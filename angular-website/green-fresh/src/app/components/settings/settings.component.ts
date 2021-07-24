import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { Fruit } from 'src/app/interfaces/fruit';
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

  // When we select a fruit to be scanned, we change this value.
  selectedOption : string = "";

  // Once the fruit value is stablish, we show the division.
  examinationStatus : Boolean = false;

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

      // SHowing the preview image.
    }
  }

  // This function is called when the selector in the html file finds a change
  selectFruit( event : Event) {
    this.selectedOption = (event.target as HTMLSelectElement).value;

    console.log(this.selectedOption)
  }

  // Whe send the data to the api 
  submit() : void {

    var data : FormData = new FormData();
    // THe form data is the one that give us the POST structure.

    // var fruit: Fruit = {
    //   code: this.uploadForm.value.code,
    //   name: this.uploadForm.value.name,
    //   description: this.uploadForm.value.description,
    //   image: this.uploadForm.value.image
    // }

    Object.entries(this.uploadForm.value).forEach(([key, value] : any[]) => {
      data.set(key,value)
    });
    
    this.api.insertFruit(data).subscribe(
      (res : any) => {
        console.log(res);
      }
    );

  }

}
