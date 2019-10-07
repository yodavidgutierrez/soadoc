import {Component} from '@angular/core';
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-dash-builder',
  templateUrl: './dash-builder.component.html',
  styleUrls: ['./dash-builder.component.css']
})
export class DashBuilderComponent  {



  constructor(private sanitizer:DomSanitizer) {

  }

  get baseUrl(){
    return  this.sanitizer.bypassSecurityTrustResourceUrl((<any>window).dashBuilderUrl);
  }

}
