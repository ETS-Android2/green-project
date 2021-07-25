import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/app/services/api.service';
import '@fortawesome/free-solid-svg-icons';
import { DomSanitizer } from '@angular/platform-browser';

// Importing the highcharts stuff
import * as Highcharts from 'highcharts'
import { EnvironmentReadings } from 'src/app/interfaces/environment-readings';
import { Fruit } from 'src/app/interfaces/fruit';
import { FruitReadings } from 'src/app/interfaces/fruit-readings';

// Declaring the requiriments for the Highcharts library

declare var require : any;

// Those are the modules implemented for the chart that we want to apply
let Boost = require('highcharts/modules/boost')
let noData = require('highcharts/modules/no-data-to-display')
let More = require('highcharts/highcharts-more')

// We have to bind the functions with the Highcharts object
Boost(Highcharts)
noData(Highcharts)
More(Highcharts)
noData(Highcharts)


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

  public options : any ={
    chart: {
        renderTo: 'container',
        type: 'column'
    },
    title: {
        text: 'Fruit Consumption'
    },
    xAxis: {
        title: {
            text: 'Fruit Number'
        },
        tickInterval: 1
    },
    yAxis: {
        title: {
            text: 'Fruit eaten'
        },
        tickInterval: 1
    },
    series: [{
        name: 'Jane',
        data: [1, 0, 4]
    }, {
        name: 'John',
        data: [5, 7, 3]
    }]
  };

  constructor(public api: ApiService) { }

  ngOnInit(){
    this.getFruits();
    this.getFruitReading();
    this.getEnvironmentReadings();
    Highcharts.chart('chart-test', this.options)
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
        if ('productionLine' in data){ // expecting an array reading
          this.fruitReadings = data;
        }
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
