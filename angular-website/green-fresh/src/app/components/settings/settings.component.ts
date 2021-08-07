import { not } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Form } from '@angular/forms';
import '@fortawesome/free-solid-svg-icons';
import { interval, Observable } from 'rxjs';
import { Fruit } from 'src/app/interfaces/fruit';
import { FruitReadings } from 'src/app/interfaces/fruit-readings';
import { ProductionLine } from 'src/app/interfaces/production-line';
import { Reading } from 'src/app/interfaces/reading';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  // The form group is the one that allow us to manage angular forms
  uploadForm : FormGroup;
  imageURL : string;

  fruits : Fruit[] = [];
  productionLines : ProductionLine[] = [];

  currentReading? : Reading;
  acceptedReadings : Reading[] = [];

  // When we select a fruit to be scanned, we change this value.
  selectedOptionFruit : string = "";
  selectedOptionPl : string = "";

  color : string = "rgb(0,0,0)";

  // The interval used for this component
  timer : any;

  constructor(private fb: FormBuilder, private api: ApiService) { 
    // Form Builder is a class that allow us to managae 
    // easily forms, is quite simple as the original class to form
    this.imageURL = "";

    this.uploadForm = this.fb.group({
      image: [null],
      name: [''],
      code: [''],
      description: ['']

    });

    // The expected body of the form 

  }


  // When the component start we call to the api service to get back all the fruits. 

  ngOnInit(): void {
    this.api.getFruits().subscribe(

      data => {
        this.fruits = data.map((fruit: Fruit) => {

          fruit.image = (fruit.image != false) ? this.api.baseURL+ "image/" + fruit.image : (fruit.image)
          // We are setting the image url as the api tells. 
          return fruit
        })
      }
    );
    
    this.api.getProductionLine().subscribe(
      data => {
        this.productionLines = data;
      }

    );

  }

  getReadings() : void {
    this.timer = setInterval( // setting the interval. 
      () => {
        this.api.getFruitReadings("productionLine="+this.selectedOptionPl).
        subscribe( (data) => {
          if ('date' in data) { // expecting just a simple reading

            if (this.currentReading == undefined){
              this.currentReading = data;
              this.color = "rgb("+data.color.R+","+data.color.G+","+data.color.B+")";
            } 

          }
        });

      }, 
      3000
    )
  }

  // Showing and setting the image when register a new fruit.
  showPreview( event : Event) : void {
    const files  = (event.target as HTMLInputElement).files;

    if (files != undefined) {
      // Accesing to the files
      const file = files[0]
      
      this.uploadForm.patchValue({
        image: file
      })
      // Biding the image into the uploadForm

      this.uploadForm.get('image')?.updateValueAndValidity()
      

      // loading the image from the url
      const reader = new FileReader()

      reader.onload = () => {
        this.imageURL = reader.result as string
      }
      
      reader.readAsDataURL(file)

      // Showing the preview image.
    }
  }

  // This function is called when the selector in the html file finds a change
  selectFruit( event : Event) {

    

    this.selectedOptionFruit = (event.target as HTMLSelectElement).value;

    if (this.selectedOptionFruit == '') return;

    clearInterval(this.timer)
    
  }

  // When the production line select

  selectProductionLine(event : Event) : void {

    clearInterval(this.timer)

    this.currentReading = undefined;
    // cleaning the current reading

    this.selectedOptionPl = (event.target as HTMLSelectElement).value;
    // Extrating the value from the selector

    if (this.selectedOptionPl == '' )  return;
    // If the selected value is empty we stop the interval
    

    let data : FormData = new FormData();
    // Preparing the form for the POST structure

    data.append("fruit", this.selectedOptionFruit);
    data.append("productionLine", this.selectedOptionPl);
    // Appending the data from the selected values


    this.api.setFruit_productionLine(data).subscribe(
      // The resquest to the API
      (res : any) => { 

        if (res.status) { 
          // If everything is good we can continue.
          this.getReadings() 
        }
      }
    )     

  }


  acceptFruit(reading : Reading) { 
    
    this.acceptedReadings.push(reading)
    this.currentReading = undefined;
  }


  // We send the data to the api 
  submit() : void {

    var data : FormData = new FormData();
    // THe form data is the one that give us the POST structure.

    Object.entries(this.uploadForm.value).forEach(([key, value] : any[]) => {
      data.set(key,value)
    });
    
    this.api.insertFruit(data).subscribe(
      (res : any) => {console.log(res); window.location.reload();}
    );
  }

  sendRequirements() : void {
    
    let data = {
      fruit: this.selectedOptionFruit,
      color: this.acceptedReadings.map((r: Reading) => { // is an array []
        return {
          R: r.color.R,
          G: r.color.G, 
          B: r.color.B
        } 
      })
    }

    this.api.insertFruitRequirements(data).subscribe(
      (res : any) => {console.log(res); window.location.reload();}
    );
    
    this.selectedOptionPl = "";
    this.selectedOptionFruit = "";

    clearInterval(this.timer);
  }

  ngOnDestroy() {
    clearInterval(this.timer);
  }

}
