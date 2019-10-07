import {Component, ViewChild} from '@angular/core';
import {ResultUploadDTO} from "./domain/ResultUploadDTO";

@Component({
    selector: 'carga-masiva',
    templateUrl: './carga-masiva.component.html',
    styleUrls: ['./carga-masiva.component.css']
})

export class CargaMasivaComponent {

    @ViewChild('records') records;

  resultUpload : ResultUploadDTO;

    onDocUploaded(result) : void {

      this.resultUpload = result;

      setTimeout( () => {this.records.getRegistros();},200);

    }

    clearError(){
      this.resultUpload = null;
    }
}
