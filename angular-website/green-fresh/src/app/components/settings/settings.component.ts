import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  uploadForm : FormGroup;
  imageURL : string;
  constructor(  private fb: FormBuilder) { 

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
    console.log(this.uploadForm.value)
  }

}
