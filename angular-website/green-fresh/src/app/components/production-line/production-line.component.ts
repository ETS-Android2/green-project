import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/app/services/api.service';
import '@fortawesome/free-solid-svg-icons';

// Importing the highcharts stuff
import * as Highcharts from 'highcharts'
import { EnvironmentReadings } from 'src/app/interfaces/environment-readings';

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

  constructor(public apiService: ApiService) { }

  ngOnInit(){
    this.getValues();
    Highcharts.chart('chart-test', this.options)
  }

  getValues(): void{
    this.apiService.getEnvironmentReadings().subscribe(
      data => {
        this.productionLines = data;
        console.log(this.productionLines);
      }, error => { console.log(error); }
    );
  }

}
