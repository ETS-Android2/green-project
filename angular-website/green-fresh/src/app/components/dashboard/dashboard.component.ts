import { Component, OnInit } from '@angular/core';
import '@fortawesome/free-solid-svg-icons';
import { DomSanitizer } from '@angular/platform-browser';
import { ApiService } from 'src/app/services/api.service';
import { ProductionLine } from 'src/app/interfaces/production-line';
import { AreaValue } from 'src/app/interfaces/area-value';
import { Fruit } from 'src/app/interfaces/fruit';

import { FormBuilder, FormGroup, FormControl, Form } from '@angular/forms';

import { FruitReadings } from 'src/app/interfaces/fruit-readings';
import { Reading } from 'src/app/interfaces/reading';
import { FruitResults } from 'src/app/interfaces/fruit-results';
import { Observable } from 'rxjs';
import { InspectionResults } from 'src/app/interfaces/inspection-results';
import { formatDate } from '@angular/common';


// Importing the highcharts stuff
import * as Highcharts from 'highcharts'


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
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {

  productionLines : ProductionLine[] = [];

  currentPl? : ProductionLine ;
  currentAreaReading? : AreaValue;
  currentFruit? : Fruit;

  colors? : { 'R' : Number[], 'G' : Number[], 'B' : Number[] } = {R: [], G: [], B: []};

  dates? : Date[] = [];

  now?: Date;

  fruits : Fruit[] = [];

  selectedOptionFruit : string = "";

  currentSeriePieChart? :  { name: string,    data: any[]} = {name: "production Line", data:[]};
  
  seriesListPieChart : any[] = [];

  months : string[] = 
  ["January", "February", "March", 
  "April", "May", "June", "July", 
  "August", "September", "October", 
  "November", "December"];

  days : string[] = 
  ["Sunday", "Monday",
   "Tuesday", "Wednesday",
  "Thursday", "Friday", "Saturday"];

  timer : any;

  resultsChart: any = {
    title: {
      text: "All fruit results by day"
      //text: (this.currentFruit != undefined && this.currentPl != undefined? this.currentFruit?.name + " status by " + this.currentPl?.description : " Undefined" ) 
    },

    subtitle: {
      text: "See if we are good!"
    },

    yAxis : {
      title: {
        text: "Fruit count"
      }
    },

    // time: {timezone: this.now?.getTimezoneOffset()},

    xAxis: {
      title: {
        text: "Time of the results by hour."
      },
      
      categories: [],
    },

    legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle'
    },  
    series: [
      {
        name: 'Results',
        data: [],
        
      },
    ]

  }
  
  plResultChart : any ={

    chart: {
      type: 'column'
    },

    title: {
      text: 'Production Line Comparison - Weekly'
    },

    xAxis: {
      categories: ['Accepted', 'Rejected']
    },

    yAxis: {
      title: {
        text: 'Accepted and Rejected Fruit'
      }
    },

    series: []
  }

  pieChart : any = {
    chart: {
      type: 'pie'
    },
    title: {
      text: 'Production Line - Fruit Status'
    },
    tooltip: {
      pointFormat: '{series.name}: <b>{point.y}pc</b>'
    },
    plotOptions: {
      pie: {
        allowPointSelect: true,
        cursor: 'pointer',
        dataLabels: {
          enabled: true,
          format: '<b>{point.name}</b>: {point.y}pc'
        }
      }
    },
  
   series: [this.currentSeriePieChart]
  }

  constructor(private api : ApiService) { 

  }

  ngOnInit(): void {

    setInterval(() => {
      this.now = new Date();
    }, 1);

    this.getProductionLines();

    this.api.getFruits().subscribe(

      data => {
        this.fruits = data.map((fruit: Fruit) => {

          fruit.image = (fruit.image != false) ? this.api.baseURL+ "image/" + fruit.image : (fruit.image)
          // We are setting the image url as the api tells. 
          return fruit
        })
      }
    );

    this.getFruitResults("period=day").then( (data) => {
    
      if ( 'dates' in data) {
        
        let fruitResult : FruitResults = data;

        this.resultsChart.title.text = "Today progress, fruit's count";
        this.resultsChart.xAxis.categories =fruitResult.dates;

        this.resultsChart.series = [ 
          { name : "Fruit Count", data : fruitResult.counts,  color: '#77E03A',
            zones : [
              {
                value: 0,
                color: '#F8002F'
              }, {
                color: '#77E03A'
              }
            ]
          }
        ]

        Highcharts.chart('chart-results-today', this.resultsChart)  
      }
  
    });


    this.getFruitResults("period=week").then((data : FruitResults | InspectionResults[]) => {
      if (Array.isArray(data)) {
        let inspectionResults : InspectionResults[] = data;

        this.plResultChart.series = [];

        if (inspectionResults.length > 0 ) {
         

          if ('productionLine' in inspectionResults[0]) {
            
            this.seriesListPieChart = [];

            inspectionResults.forEach((ir : InspectionResults) => {

              if (ir.productionLine != undefined) {
                this.plResultChart.series.push( {
                  name: ir.productionLine.description,
                  data: [ir.results.accepted, ir.results.rejected]
                });
                
                this.currentSeriePieChart = { 
                  name: ir.productionLine.description,
                  data: [{
                    name:"Accepted",
                    y: ir.results.accepted,
                    color: "#77E03A"
                  },{
                    name:"Rejected",
                    y: ir.results.rejected,
                    color: "#F8002F"
                  }]
                }
  
                this.seriesListPieChart.push(this.currentSeriePieChart)
              }

              

            })
            
            if (this.seriesListPieChart.length > 0) {
              this.currentSeriePieChart = this.seriesListPieChart[0];
              
              this.pieChart.title.text = this.currentSeriePieChart?.name + " - Fruit Selection"
              this.pieChart.series = [this.currentSeriePieChart]

            }

          }
        }

        Highcharts.chart('chart-results-week', this.plResultChart)
        Highcharts.chart('chart-results-pie', this.pieChart)
      }
    });
    
  }

  getProductionLines() {
    this.api.getProductionLine().subscribe(
      data => {
        this.setProductionLine(data[0])
        this.productionLines = data;

      }
    )
    
  }

  getFruitResults(period: string) {
    return new Promise<InspectionResults[] | FruitResults >( resolve => {
      this.api.getInpectionResults(period).subscribe( data => {
        resolve(data);
      })
    })
  }

  getFruitReadings() :void{
    this.api.getFruitReadings("search="+this.currentPl?.code+"&fruit="+this.currentFruit?.code).
    subscribe(
      (data : FruitReadings[] | Reading)  => {
        
        if (Array.isArray(data)) { // Checking if is an array

          let readings : FruitReadings[]= data; // We need a variable to corfirm the data type
          
          this.colors = {R:[], G:[], B:[]};
          this.dates = [];

          if (readings.length > 0){ // Verifiying that it is not empty

            readings[0].readings.forEach( (r : Reading) => {

              this.colors?.R.push(r.color.R);
              this.colors?.G.push(r.color.G);
              this.colors?.B.push(r.color.B);

              
              this.dates?.push(new Date(r.date+"-0700"))
            })
 
            
          }

          let fruitLineChart: any = {
            title: {
              text: this.currentFruit?.name + " status by " + this.currentPl?.description 
              //text: (this.currentFruit != undefined && this.currentPl != undefined? this.currentFruit?.name + " status by " + this.currentPl?.description : " Undefined" ) 
            },
        
            subtitle: {
              text: "Check the RGB values"
            },
        
            yAxis : {
              title: {
                text: "RGB values (0 - 255)"
              }
            },

            // time: {timezone: this.now?.getTimezoneOffset()},
        
            xAxis: {
              title: {
                text: "Time of the readings."
              },
              accessibility : {
                rangeDescription: "Time of the readigns."
              },
              type: 'time',
              labels : {
                format: '{value: %H:%M:%S}'
              },
              categories: this.dates?.map((d) => d.getHours()+":"+d.getMinutes()),
            },
        
            legend: {
              layout: 'vertical',
              align: 'right',
              verticalAlign: 'middle'
            },
        
            // plotOptions : {
            //   series: {
            //     label: {
            //       connectorAllowed: true
            //     },
        
            //     pointStart: 0
            //   }
            // },
        
            series: [
              {
                name: 'R',
                data: this.colors?.R,
                color: '#F8002F'
              },
              {
                name: 'G',
                data: this.colors?.G,
                color: '#77E03A'
              },
              {
                name: 'B',
                data: this.colors?.B,
                color: '#728FCE'
              }
            ]
        
          }
          
          Highcharts.chart('chart-test', fruitLineChart)

        }
        
      }
    )
  }

  setProductionLine(pl : ProductionLine) {

    this.currentPl = pl;

    this.api.getEnvironmentReadings("productionLine="+this.currentPl.code).
    subscribe(data => {
      if ('temperature' in data){
        this.currentAreaReading = data;
      }
    });

    
    this.api.getRelation("productionLine="+this.currentPl.code).
    subscribe(data => {
      
      data.fruit.image = (data.fruit.image != false) ? this.api.baseURL+ "image/" + data.fruit.image : (data.fruit.image) ;
      this.currentFruit = data.fruit;

      clearInterval(this.timer);

      this.getFruitReadings()

      this.timer = setInterval( () => 
        {
          this.getFruitReadings();
        },
        1000*30*1        
      )

    })
  }

  selectFruit( event : Event) {

    this.selectedOptionFruit = (event.target as HTMLSelectElement).value;

    if (this.selectedOptionFruit == '') return;
    
  }

  setFruit() {
    let formData : FormData = new FormData();

    if (this.currentPl != undefined) {
      formData.append("fruit", this.selectedOptionFruit);
      formData.append("productionLine", this.currentPl?.code);

      this.api.setFruit_productionLine(formData).subscribe(
        // The resquest to the API
        (res : any) => { 
          console.log(res)
          this.getProductionLines()
        }
      )   
    }

    
  }
  
  changePieChartItem( index : number) {

    let position = this.seriesListPieChart.indexOf(this.currentSeriePieChart) + (index);

    if ( position > this.seriesListPieChart.length -1) {
      
      position = 0;
    }


    if (position < 0 ) {
      
      position = this.seriesListPieChart.length -1 ;
    }

    this.currentSeriePieChart = this.seriesListPieChart[position];

    this.pieChart.title.text = this.currentSeriePieChart?.name + " - Fruit Selection"
    this.pieChart.series = [this.currentSeriePieChart]
    Highcharts.chart('chart-results-pie', this.pieChart)
    console.log(this.currentSeriePieChart)

  }

  ngOnDestroy() {
    clearInterval(this.timer);
  }

}
