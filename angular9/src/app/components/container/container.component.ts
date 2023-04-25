import { Component } from '@angular/core';
import { ConfigService } from '../../httprequest.service';
import { IMultiSelectItem } from "../multiselect/multiselect.component";
import { lastValueFrom } from "rxjs";

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent {
  currentIp:string = "";
  availableIPs:any=[];
  acquiredIPs:any=[];

  constructor(private service: ConfigService) {
    this.getAllAddresses();
  }

  //Update currentIp from input
  newIp(ip:string)
  {
    this.currentIp = ip;
  }

  //REST calls

  //separate both acquired/available ips
  getAllAddresses()
  {
    this.service.getAllAddresses().subscribe(data =>
    {
      let availIpList:any=[];
      let acquireIpList:any=[];
      data.forEach(elem =>
      {
        //Format Data into multiselectobj
        let multiSelectObj: IMultiSelectItem={viewValue:"",value:""};
        multiSelectObj.viewValue=elem.ip;
        multiSelectObj.value=elem.ip;
        if(elem.status === "available")
        {
          availIpList.push(multiSelectObj);
        }
        else
        {
          acquireIpList.push(multiSelectObj);
        }
      })
      this.availableIPs = availIpList;
      this.acquiredIPs = acquireIpList;
    });
  }

  //Synchronously add CIDR IP
  async addIp()
  {
    if(this.currentIp!="") {
      let data = await lastValueFrom(this.service.addIp(this.currentIp));
      this.getAllAddresses();
    }

  }

  //Synchronously acquire IP
  async acquireIp(ips:any)
  {
    //allow weak check
    if(Array.isArray(ips) && ips.length > 0)
    {
      for (const ip of ips)
      {
        let data = await lastValueFrom(this.service.acquireIp(ip));
      }
      this.getAllAddresses();
    }
    else
    {
      //No need to send
    }
  }

  //Synchronously release IP
  async releaseIp(ips:any)
  {
    //allow weak check
    if(Array.isArray(ips) && ips.length > 0)
    {
      for (const ip of ips)
      {
        let data = await lastValueFrom(this.service.releaseIp(ip));
      }
      this.getAllAddresses();
    }
  }
}
