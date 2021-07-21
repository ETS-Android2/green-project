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

  uploadForm : FormGroup;
  imageURL : string;
  constructor(private fb: FormBuilder, private api: ApiService) { 

    this.imageURL = "";

    this.uploadForm = this.fb.group({
      image: [null],
      name: [''],
      code: [''],
      description: ['']

    });
  }



  ngOnInit(): void {

  }


  showPreview( event : Event) : void {
    const files  = (event.target as HTMLInputElement).files;

    if (files != undefined) {
      const file = files[0]
      
      this.uploadForm.patchValue({
        image: file
      })


      this.uploadForm.get('image')?.updateValueAndValidity()

      // loading the image from the url
      const reader = new FileReader()

      reader.onload = () => {
        this.imageURL = reader.result as string
      }

      reader.readAsDataURL(file)

    }
  }

  submit() : void {

    var data : FormData = new FormData();

    var fruit: Fruit = {
      code: this.uploadForm.value.code,
      name: this.uploadForm.value.name,
      description: this.uploadForm.value.description,
      image: this.uploadForm.value.image
    }

    Object.entries(this.uploadForm.value).forEach(([key, value] : any[]) => {
      data.set(key,value)
    });

    this.api.setFruit(data).subscribe(
      res => {
        console.log(res);
      }
    );

  }

}
