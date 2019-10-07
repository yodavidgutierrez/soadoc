import {Component, OnInit, Input, OnChanges, Output, EventEmitter, SimpleChanges} from '@angular/core';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';
import {ARCHIVO_CENTRAL} from "../../../../../shared/bussiness-properties/radicacion-properties";
import {isNullOrUndefined} from "util";
import {PushNotificationAction} from "../../../../../infrastructure/state-management/notifications-state/notifications-actions";
import {Store} from "@ngrx/store";
import  {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {UnidadConservacionDTO} from "../../../../../domain/UnidadConservacionDTO";
import {ConfirmationService} from "primeng/primeng";

@Component({
  selector: 'app-detalle-unidad-conservacion',
  templateUrl: './detalle-unidad-conservacion.component.html',
  styleUrls: ['./detalle-unidad-conservacion.component.css']
})
export class DetalleUnidadConservacionComponent implements OnInit,OnChanges {

  @Input() state: StateUnidadDocumentalService;

  @Input() innerComponent = false;

  @Input() allowLocate = false;

  @Input() selectedIdx;

  @Output() onNoFound = new EventEmitter();

  readonly tipoArchivo = ARCHIVO_CENTRAL;

  selectedIndex = -1;

  dialogVisible = false;

  ubicacionFisica;

  validUbicacionFisica = false;

  ubicacionesVisited:any  = {};

  constructor(private confirmService:ConfirmationService) { }

  ngOnInit() {

  }



  descriptionSerieSubserie( item){

    if(isNullOrUndefined(item))
      return;

    if(item.codigoSubSerie)
      return `${item.codigoSerie} - ${item.nombreSerie} / ${item.codigoSubSerie} - ${item.nombreSubSerie}`;

    return `${item.codigoSerie} - ${item.nombreSubSerie}`;
  }

  get selectedUnidadConservacion(){

    return !isNullOrUndefined(this.UnidadSeleccionada) ? this.UnidadSeleccionada.unidadesConservacion[this.selectedIndex]: null;
  }

  showUbicarDialog(index){
    this.selectedIndex = index;
    this.dialogVisible = true;
  }

  get UnidadesConservacion():UnidadConservacionDTO[]{

    return this.state.ListadoUnidadDocumental.reduce((prev,curr) => {

      if(!isNullOrUndefined(curr.unidadesConservacion))
        prev =  prev.concat(curr.unidadesConservacion);

      return prev;
    },[]);
  }

  saveUnidadConservacion(force = false){

    if(!force && this.UnidadesConservacion.some(uc => uc.ideUbicFisiBod == this.ubicacionFisica.carpeta.ide && uc.ideBodega ==  this.ubicacionFisica.bodega.ide)){
      this.confirmService.confirm({
        message: "¿Ya existe una unidad de conservación con esta ubicación física.Seguro desea ubicarlo aqui?",
        header: "Confirmacion",
        icon : 'fa fa-question-circle',
        accept: _ => { this.saveUnidadConservacion(true)},
        reject: _ => {}
      });

      return;
    }

    this.selectedUnidadConservacion.ideBodega = this.ubicacionFisica.bodega.ide;
    this.selectedUnidadConservacion.ideUbicFisiBod = this.ubicacionFisica.carpeta.ide;

    if(isNullOrUndefined(this.ubicacionesVisited[this.selectedIdx]))
       this.ubicacionesVisited[this.selectedIdx] = [];

    this.ubicacionesVisited[this.selectedIdx].push(this.selectedIndex);
    if(this.UnidadSeleccionada.soporte == 'Hibrido')
      this.UnidadSeleccionada.ubicacionTopografica = this.ubicacionFisica.carpeta.ubicacion;

    console.log(this.state.ListadoUnidadDocumental);
    this.dialogVisible = false;
    this.selectedIndex = -1;
  }

  updateUnidadConservacion(event){

    this.ubicacionFisica = event.value;
    this.validUbicacionFisica = event.isValid;
  }

  showNoFoundUnidadesConservacionNotification(){
      this.onNoFound.emit();
  }

 get  UnidadSeleccionada(){

    return   this.state.ListadoUnidadDocumental[this.selectedIdx]
  }

  ngOnChanges(changes: SimpleChanges): void {

    if(isNullOrUndefined(changes.selectedIdx)){

    }
  }

  hasVisitedIndex(udIndex,ucIndex){

    const conditions:boolean[] = [
                                  ucIndex ==this.selectedIndex  && udIndex == this.selectedIdx,
                                  !isNullOrUndefined(this.ubicacionesVisited[udIndex]) && this.ubicacionesVisited[udIndex].includes(ucIndex)];

    return conditions.some( c => c);
  }


}
