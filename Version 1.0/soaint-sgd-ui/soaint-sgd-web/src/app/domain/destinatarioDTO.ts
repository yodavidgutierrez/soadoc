import {ConstanteDTO} from './constanteDTO';
import {FuncionarioDTO} from './funcionarioDTO';
import {PaisDTO} from './paisDTO';
import {DepartamentoDTO} from './departamentoDTO';
import {MunicipioDTO} from './municipioDTO';
import {ContactoDTO} from './contactoDTO';

export interface DestinatarioDTO {
    interno: boolean,
    tipoDestinatario: ConstanteDTO,
    tipoPersona?: ConstanteDTO,
    razonSocial?: string,
    nroDocumentoIdentidad?: string,
    nombre?: string,
    tipoDocumento?: ConstanteDTO,
    nit?: string,
    actuaCalidad?: ConstanteDTO,
    actuaCalidadNombre?: string,
    sede?: ConstanteDTO,
    dependencia?: ConstanteDTO,
    funcionario?: FuncionarioDTO,

    email?: string,
    mobile?: string,
    phone?: string,
    pais?: PaisDTO,
    departamento?: DepartamentoDTO,
    municipio?: MunicipioDTO,
    datosContactoList?: ContactoDTO[],
    isBacken?: boolean,
    ideAgente?:string;
}

