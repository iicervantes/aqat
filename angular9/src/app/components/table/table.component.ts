import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent {
  @Input() dataSource:any=[];
  displayedColumns:string[] = ["coordinates","location",'measurement',"lastUpdated"];
}
