import {Component, ViewChild} from '@angular/core';
import { ConfigService } from '../../httprequest.service';
import {IMultiSelectItem, MultiselectComponent} from "../multiselect/multiselect.component";

@Component({
  selector: 'app-aqatcontainer',
  templateUrl: './aqatcontainer.component.html',
  styleUrls: ['./aqatcontainer.component.css']
})
export class AqatcontainerComponent {
  citiesLeft:any=[];
  citiesRight:any=[];
  cityLeftData:any=[];
  cityRightData:any=[];

  @ViewChild('cityLeftChild', {static : true}) cityLeftChild : MultiselectComponent | undefined;
  @ViewChild('cityRightChild', {static : true}) cityRightChild : MultiselectComponent | undefined;

  constructor(private service: ConfigService) {
    this.getCities();
  }

  //btnclick calls child getSelectedItems which emits data back to parent
  compare()
  {
    if(this.cityLeftChild)
      this.cityLeftChild.getSelectedItems();
    if(this.cityRightChild)
      this.cityRightChild.getSelectedItems();
  }
  //REST calls
  getCities()
  {
    let city: IMultiSelectItem[] = [];
    this.service.getCities().subscribe(data =>{
      data.results.forEach((elem:any) => {
        let selectItem: IMultiSelectItem = {viewValue:"",value:""};
        selectItem.viewValue = elem.city;
        selectItem.value = elem.city;
        this.citiesLeft.push(selectItem);
        this.citiesRight.push(selectItem);
      });
    });
  }
  getCityLeftInfo(city:any)
  {
    this.cityLeftData = [];
    this.getCityInfo(this.cityLeftData, city);
  }
  getCityRightInfo(city:any)
  {
    this.cityRightData = [];
    this.getCityInfo(this.cityRightData, city);
  }

  //helper getcitymeasurements
  getCityInfo(cityData:any, city:string)
  {
    if(city){
      this.service.getCityMeasurements(city).subscribe(data=>{
        for(let cityPoints of data.results){
          let item:any={}
          item.coordinates = cityPoints.coordinates.latitude + ", " + cityPoints.coordinates.longitude;
          item.location = cityPoints.location;
          for(let elem of cityPoints.measurements) {
            item.measurement = elem.parameter.toUpperCase() + ": " + elem.value + " " + elem.unit;
            item.lastUpdated = elem.lastUpdated;
            cityData.push({...item});
          }
        }
      })
    }
  }
}
