import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/app/services/api.service';
import '@fortawesome/free-solid-svg-icons';

import { EnvironmentReadings } from 'src/app/interfaces/environment-readings';
import { Fruit } from 'src/app/interfaces/fruit';
import { FruitReadings } from 'src/app/interfaces/fruit-readings';

@Component({
  selector: 'app-production-line',
  templateUrl: './production-line.component.html',
  styleUrls: ['./production-line.component.css']
})

export class ProductionLineComponent implements OnInit {

  // attributes
  public fruits: Fruit[] = [];
  public fruitReadings: FruitReadings[] = [];
  public productionLines: EnvironmentReadings[] = [];

  constructor(public api: ApiService) { }

  ngOnInit(){
    this.getFruits();
    this.getFruitReading();
    this.getEnvironmentReadings();
    
  }

  getFruits(): void{
    this.api.getFruits().subscribe(
      data => {
        
        this.fruits = data.map((fruit: Fruit) => {
          fruit.image = (fruit.image != false) ? this.api.baseURL+ "image/" + fruit.image : (fruit.image)

          return fruit
        })
      }
    );
  }

  getFruitReading(): void{
    this.api.getFruitReadings().subscribe(
      data => {
        this.fruitReadings = data;
      }, error => { console.log(error); }
    );
  }

  getEnvironmentReadings(): void{
    this.api.getEnvironmentReadings().subscribe(
      data => {
        this.productionLines = data;
        // delete this later, is an example
        console.log(this.productionLines);
        console.log(this.productionLines[0].productionLine.status.value);
      }, error => { console.log(error); }
    );
  }
}
