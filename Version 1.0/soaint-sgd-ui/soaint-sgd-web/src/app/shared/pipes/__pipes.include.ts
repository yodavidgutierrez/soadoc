// import {NgModule} from '@angular/core';
//
import {EllipsisPipe} from './ellipsis';
import {EstadoAsignacion} from './estadoAsignacion';
import {
  DropdownItemPipe, DropdownItemPipeFullName, DropdownItemPipeSerie, DropdownItemPipeSingle,
  DropdownItemPipeSubserie, DropdownItemPipeString
} from './dropdown-item';
import {DropdownSingleItemPipe} from './dropdown-single-item';
import {ConstantCodePipe} from './constant-code-pipe.pipe';
import { CountryPhonePipe } from './countryPhone-input.pipe';
import { MobilePhonePipe } from './mobile-input.pipe';
import { DateFormatPipe, DateTimeFormatPipe } from './date.pipe';
import { ToActiveString } from './boolean-transform.pipe';
import {TipologiaDocumentalPipePipe} from "./tipologia-documental-pipe.pipe";
import {SerieNamePipe} from "./serie.pipe";
import {FuncionarioNombreCompletoPipe} from "./funcionario-nombre-completo.pipe";
import {ConstantNamePipe} from "./constant-name.pipe";
import {NiceNoRadicadoPipe} from "./nice-no-radicado.pipe";
import {FindFuncionarioIdPipe} from "./find-funcionario-id.pipe";
import {MegafStructurePipe} from "./megaf-structure.pipe";
import {DependenciaNombrePipe} from "./dependencia-nombre.pipe";
import {FileTypePipe} from "./file-type.pipe";
import {FuncionarioValorPipe} from "./funcionario-valor.pipe";
import {Utf8EncodePipe} from "./utf8Encode.pipe";

export const PIPES = [
    EstadoAsignacion,
    EllipsisPipe,
    DropdownItemPipe,
    DropdownSingleItemPipe,
    DropdownItemPipeFullName,
    ConstantCodePipe,
    CountryPhonePipe,
    MobilePhonePipe,
    DateFormatPipe,
    DateTimeFormatPipe,
    DropdownItemPipeSerie,
    DropdownItemPipeSubserie,
    DropdownItemPipeSingle,
    DropdownItemPipeString,
    ToActiveString,
    TipologiaDocumentalPipePipe,
    SerieNamePipe,
  FuncionarioNombreCompletoPipe,
  ConstantNamePipe,
  NiceNoRadicadoPipe,
  FindFuncionarioIdPipe,
  MegafStructurePipe,
  DependenciaNombrePipe,
  FileTypePipe,
  FuncionarioValorPipe,
  Utf8EncodePipe
];

export const PIPES_AS_PROVIDERS = [
  CountryPhonePipe,
  MobilePhonePipe
];
//
// @NgModule({
//   declarations: PIPES,
//   exports: PIPES
// })
// export class PipesModule {
// }
