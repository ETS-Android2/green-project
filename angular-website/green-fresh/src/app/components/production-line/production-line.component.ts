import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/app/services/api.service';
import '@fortawesome/free-solid-svg-icons';

import { EnvironmentReadings } from 'src/app/interfaces/environment-readings';
import { Fruit } from 'src/app/interfaces/fruit';
import { FruitReadings } from 'src/app/interfaces/fruit-readings';

// Importing the highcharts stuff
import * as Highcharts from 'highcharts'
import { Reading } from 'src/app/interfaces/reading';
import { ThrowStmt } from '@angular/compiler';
import { InspectionResults } from 'src/app/interfaces/inspection-results';
import { FruitResults } from 'src/app/interfaces/fruit-results';


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

  currentFruit? : Fruit;

  plotFruit? : Fruit; // The current fruit in the plot chart.




  weightsChart: any = {
    title: {
      text: "Fruit Name - Weight relation"
      //text: (this.currentFruit != undefined && this.currentPl != undefined? this.currentFruit?.name + " status by " + this.currentPl?.description : " Undefined" ) 
    },

    subtitle: {
      text: "Good weight and shape!"
    },

    yAxis : {
      title: {
        text: "Weight"
      }
    },


    xAxis: {
      title: {
        text: "Time of the results by hour."
      },
      type: 'time',
      labels : {
        format: '{value: %H:%M:%S}'
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
        name: 'Weight',
        data: [],
        
      },
    ]

  }

  fruitsResults: any = {
    title: {
      text: "Fruit Progress"
      //text: (this.currentFruit != undefined && this.currentPl != undefined? this.currentFruit?.name + " status by " + this.currentPl?.description : " Undefined" ) 
    },

    subtitle: {
      text: "We have different data about fruit!"
    },

    yAxis : {
      title: {
        text: "No. Pieces"
      }
    },


    xAxis: {
      title: {
        text: "Time of the results by hour."
      },
      type: 'time',
      labels : {
        format: '{value: %H:%M:%S}'
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
        name: 'Fruit',
        data: [],
        
      },
    ]

  }

  plotChart: any = {
    chart: {
      type: 'scatter',
      zoomtype: 'xy'
    },

    title: {
      text: 'Fruit - RGB selection'
    },

    subtitle: {
      text: 'So many plots.'
    },

    xAxis: {
      title: {
        text: 'Time',
        
        enabled: true
      },
      labels: {
        formatter: (e : any) : string => {
          let d : Date = new Date(e.value)
          
          return d.getHours()+":"+d.getSeconds()
          
        }
      }
      
    },

    yAxis: {
      title: {
        text: 'RGB',
      }
    },

    legend: {
      layout: 'vertical',
      align: 'left',
      verticalAlign: 'top',
      x: 100,
      y: 70,
      floating: true,
      borderWidth: 1
    },

    plotOptions: {
      scatter: {
        marker: {
          radius: 5,
          states: {
            hover: {
              enabled: true,
              lineColor: 'rgb(100,100,100)'
            }
          }
        },
        states: {
          hover: {
            marker: {
              enabled: false
            }
          }
        },
        tooltip: {
          enabled: true,
          pointFormat:'{series.name}: {point.y}, Time { point.x }',
          //pointFormat:'{series.name}: {point.y}, Time { new Date(point.x).getHours().toString()}: {new Date(point.x).getSeconds().toString()}',
          // pointFormatter: (e : any) : string => {

          //   let d : Date = new Date(e.point.x)

          //   return e.series.name+':'+ e.point.y+', Time '+ d.getHours()+":"+d.getSeconds()
          // } 
        }
      }
    },

    series: [{
      name: 'Female',
      color: 'rgba(223, 83, 83, .5)',
      data: [[161.2, 51.6]]
    }]
  }

  constructor(public api: ApiService) { }

  ngOnInit(){
    this.getFruits();
    // this.getFruitReading();
    this.getEnvironmentReadings();

    this.getInpectionResults("period=day-all").then((data: InspectionResults[] | FruitResults) => {
      if (Array.isArray(data)) {
        let readings : InspectionResults[] = data;
        this.fruitsResults.xAxis.categories = [];
        this.fruitsResults.series = [];

        let series: any[] = []

        if ( readings.length > 0) {
          if ('results' in readings[0]) {          

            readings.map( r => {
              let  res = series.find(({ date }) => date === r.date);

              if (res == undefined){
                series.push({ date: r.date})
                res = series.find((element) => {
                  return element.date == r.date
                })

              } 

              let index = series.indexOf(res)

              let seriesFruit =  series.find((element) => {
                if (r.fruit?.name != undefined) {
                  return element[r.fruit?.name] === r.results.accepted && element["date"] === r.date
                }  else return undefined
              })


              if (r.fruit?.name != undefined) {
                if (seriesFruit == undefined) {
                  
                  series[index][r.fruit?.name] =  r.results.accepted;
                  
                } else series[index][r.fruit?.name] = series[index][r.fruit?.name] + r.results.accepted
              }
        

            })

            if (series != []) {

              series.forEach((item, index) => {
                Object.entries(item).forEach( ([key, value] : any[]) => {
                  if (!(key in series[0])) {
                    series[0][key] = 0;
                  }
                })    
              })                

              series = series.map((e, i, arr) => {
                if (i > 0 ) {
                  Object.entries(arr[i-1]).forEach( ([key, value] : any[]) => {
                    if (!(key in e)) {
                      e[key] = value;
                    }
                  })
           
                }
                return e
              })

              this.fruitsResults.xAxis.categories = series.map(e => e.date);
              this.fruitsResults.series = []
              
              Object.entries(series[0]).forEach(
                ( e, i, arr ) => {

                  let key : string, value : any;

                  [key, value] = e

                  if (key != "date") this.fruitsResults.series.push(
                    { 
                      name : key, 
                      data: series.map(e => e[key]),                       
                    }
                  )

                }
              );

            }
            
          }          
        }

        Highcharts.chart('inspection-result-day', this.fruitsResults)

      }
    })
        
  }

  getFruits(): void{
    this.api.getFruits().subscribe(
      data => {

        this.fruits = data.map((fruit: Fruit) => {
          fruit.image = (fruit.image != false) ? this.api.baseURL+ "image/" + fruit.image : (fruit.image)

          return fruit
        })

        this.setFruit(this.fruits[0])
        this.plotFruit = this.fruits[0]        
        this.showPlotChart()
      }
    );
  }

  getFruitReading(params : string ="" ){

    return new Promise<FruitReadings[] | Reading>(resolve => {
      this.api.getFruitReadings(params).subscribe(
        data => {
          
          resolve(data)

        }, error => { console.log(error); }
      );
    })
    
  }

  getEnvironmentReadings(): void{
    this.api.getEnvironmentReadings().subscribe(
      data => {
        if ('productionLine' in data) {
          this.productionLines = data;
        // delete this later, is an example
  
        }
        
      }, error => { console.log(error); }
    );
  }

  getInpectionResults(period="") {
    return new Promise<InspectionResults[] | FruitResults >(resolve => {
      this.api.getInpectionResults(period).subscribe((data: InspectionResults[] | FruitResults) => {
        resolve(data)
      })
    })
  }

  setFruit( fruit : Fruit) {
    this.currentFruit = fruit;

    this.showWeightChart()

    setInterval(() => {
      this.showWeightChart()
    }, 1000*20)

  }


  changeItem( index : number) {

  
    let position = 0
    if (this.plotFruit != undefined) {
      position = this.fruits.indexOf(this.plotFruit) + (index);
    } else return
    

    if ( position > this.fruits.length -1)  position = 0;


    if (position < 0 ) position = this.fruits.length -1 ;
    

    this.plotFruit =  this.fruits[position]

    this.showPlotChart()

  
  }

  showPlotChart() {
    if (this.plotFruit != undefined) 
    this.getFruitReading("fruit="+this.plotFruit.code).
    then( (data : Reading | FruitReadings[]) => {
      if (Array.isArray(data)) {
        let readings : FruitReadings[] = data;

        this.plotChart.title.text = 'Fruit - RGB selection'
        this.plotChart.series = []

        let r_series : any[] = []
        let g_series : any[] = []
        let b_series : any[] = []

        let dates : Date[] = []
        

        if (readings.length >0 )
        if ('readings' in readings[0]) {
          this.plotChart.title.text = this.plotFruit?.name+' - RGB selection'
          readings.forEach(r  => {
            r.readings.forEach( e => {

              r_series.push(e.color.R)
              g_series.push(e.color.G)
              b_series.push(e.color.B)                          

              dates.push(new Date(e.date+"-0700"))

            })
          })

          this.plotChart.series = [
            { 
              
              name: "Red", 
              color: 'rgb(255, 0, 0)',
              data: r_series.map((e, i) => {
               return [dates[i].getTime(), e] 
              }),
            },
            { 
              
              name: "Green", 
              color: 'rgb(0, 255, 0)',
              data: g_series.map((e, i) => {
               return [dates[i].getTime(), e] 
              }),
            },
            { 
              
              name: "Blue", 
              color: 'rgb(0, 0, 255)',
              data: b_series.map((e, i) => {
               return [dates[i].getTime(), e] 
              }),
            }
          ]

        }


        Highcharts.chart('plot-chart', this.plotChart)
      }
    })

  }

  showWeightChart() {

    if (this.currentFruit != undefined) {
      this.getFruitReading("fruit="+this.currentFruit.code).then((data : FruitReadings[] | Reading) => {
        if (Array.isArray(data)) {
    
          let readings : FruitReadings[] = data;
    
          this.weightsChart.title.text = this.currentFruit?.name +" - Weight Relation"
          this.weightsChart.xAxis.categories = [];
          this.weightsChart.series = []
    
    
          if (readings.length > 0) {
            if ('readings' in readings[0]) {
    
              this.fruitReadings = readings;
    
              let series : any[] = []
    
              readings.forEach((pl : FruitReadings ) => {
                pl.readings.forEach((r : Reading) => {
                  series.push({  weight : r.weight.value, date: new Date(r.date+"-0700")})
                })
              })
    
              series.sort((a, b) => a.date - b.date)
    
              // console.log(series)
              // this.dates?.push(new Date(r.date+"-0700"))
              

    
              this.weightsChart.xAxis.categories = series.map((s) => s.date.getHours()+":"+s.date.getMinutes());
              this.weightsChart.series = [{name: "Weight", data : series.map((s) => s.weight)}]
    
              
    
            }
          }
    
          Highcharts.chart('chart-weight', this.weightsChart)
    
        }
      })
    }

    
  }

}

