import { Component, OnInit, ViewChild } from '@angular/core';

import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexPlotOptions,
  ApexXAxis,
  ApexFill,
  ApexDataLabels,
  ApexYAxis,
  ApexGrid
} from "ng-apexcharts";
import { ApiService } from 'src/app/services/api.service';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  fill: ApexFill;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  plotOptions: ApexPlotOptions;
};

@Component({
  selector: 'app-gantt',
  templateUrl: './gantt.component.html',
  styleUrls: ['./gantt.component.css']
})
export class GanttComponent implements OnInit {

  @ViewChild("chart") chart!: ChartComponent;
  public chartOptions: Partial<ChartOptions>;

  

  constructor(private api: ApiService) {
    this.chartOptions = {
      series: [  ],
      chart: {
        height: 350,
        type: "rangeBar"
      },
      plotOptions: {
        bar: {
          horizontal: true,
          distributed: true,
        }
      },
      xaxis: {
        type: "datetime"
      },
      grid: {
        row: {
          colors: ["#f3f4f5", "#fff"],
          opacity: 1
        }
      }
    };
  }
  ngOnInit(): void {
    this.getAllTasks()
    this.initializeChart()
  }

  initializeChart(){
    this.chartOptions = {
      series: [
        // {
        //   data: [
        //     // {
        //     //   x: "Analysis",
        //     //   y: [
        //     //     new Date("2019-02-27").getTime(),
        //     //     new Date("2019-03-04").getTime()
        //     //   ],
        //     //   fillColor: "#008FFB"
        //     // },
        //     // {
        //     //   x: "Design",
        //     //   y: [
        //     //     new Date("2019-03-04").getTime(),
        //     //     new Date("2019-03-08").getTime()
        //     //   ],
        //     //   fillColor: "#00E396"
        //     // },
        //     // {
        //     //   x: "Coding",
        //     //   y: [
        //     //     new Date("2019-03-07").getTime(),
        //     //     new Date("2019-03-10").getTime()
        //     //   ],
        //     //   fillColor: "#775DD0"
        //     // },
        //     // {
        //     //   x: "Testing",
        //     //   y: [
        //     //     new Date("2019-03-08").getTime(),
        //     //     new Date("2019-03-12").getTime()
        //     //   ],
        //     //   fillColor: "#FEB019"
        //     // },
        //     // {
        //     //   x: "Deployment",
        //     //   y: [
        //     //     new Date("2019-03-12").getTime(),
        //     //     new Date("2019-03-17").getTime()
        //     //   ],
        //     //   fillColor: "#FF4560"
        //     // }
        //   ]
        // }
      ],
      chart: {
        height: 350,
        width: 700,
        type: "rangeBar"
      },
      plotOptions: {
        bar: {
          horizontal: true,
          distributed: true,
        }
      },
      xaxis: {
        type: "datetime"
      },
      grid: {
        row: {
          colors: ["#f3f4f5", "#fff"],
          opacity: 1
        }
      }
    };
    console.log(this.chartOptions.series);
    
  }

  tasks!: any;

  getAllTasks(){
    this.api.getTask(1,this.api.AccessTokenThrow()).subscribe(res=>{
      this.tasks = res;
      console.log(this.tasks);
      for(let row of this.tasks){
        let x:any = row.name;
        let y1:Date  =row.startDate;
        let y2: Date = row.deadline;
        let fillcolor = "#FF4560"
        let type:any = [y1.getTime(),y2.getTime()]

        let data = [x,fillcolor,type]
        this.chartOptions.series?.push(
          {data}
          )
          console.log(data);
      }
      console.log(this.chartOptions.series);
      
    })
    
  }

}
