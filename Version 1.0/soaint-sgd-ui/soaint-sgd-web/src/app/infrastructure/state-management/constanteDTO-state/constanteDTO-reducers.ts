import {Actions, ActionTypes as Autocomplete} from './constanteDTO-actions';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {tassign} from 'tassign';

interface ConstanteDTOStateInterface {
  ids: number[];
  entities: { [id: string]: ConstanteDTO };
  selectedId?: number | null;
  filter?: string;
}

class ConstanteDTOStateInstance {
  ids = [];
  entities = {};
  selectedId = null
}

export interface State {
  tipoComunicacion: ConstanteDTOStateInterface;
  tipoPersona: ConstanteDTOStateInterface;
  tipoAnexos: ConstanteDTOStateInterface;
  tipoDocumento: ConstanteDTOStateInterface;
  tipoTelefono: ConstanteDTOStateInterface;
  tipoDestinatario: ConstanteDTOStateInterface;
  unidadTiempo: ConstanteDTOStateInterface;
  mediosRecepcion: ConstanteDTOStateInterface;
  tipologiaDocumental: ConstanteDTOStateInterface;
  tratamientoCortesia: ConstanteDTOStateInterface;
  tipoVia: ConstanteDTOStateInterface;
  prefijoCuadrante: ConstanteDTOStateInterface;
  bis: ConstanteDTOStateInterface;
  orientacion: ConstanteDTOStateInterface;
  tipoComplemento: ConstanteDTOStateInterface;
  actuaCalidad: ConstanteDTOStateInterface;
  causalDevolucion: ConstanteDTOStateInterface;
  soporteAnexo: ConstanteDTOStateInterface;
  modalidadCorreo: ConstanteDTOStateInterface;
  claseEnvio: ConstanteDTOStateInterface;
  motivoNoCreacionUD : ConstanteDTOStateInterface;
  tipoContacto:ConstanteDTOStateInstance
}

const initialState: State = {
  tipoComunicacion: new ConstanteDTOStateInstance(),
  tipoPersona: new ConstanteDTOStateInstance(),
  tipoAnexos: new ConstanteDTOStateInstance(),
  tipoDocumento: new ConstanteDTOStateInstance(),
  tipoTelefono: new ConstanteDTOStateInstance(),
  tipoDestinatario: new ConstanteDTOStateInstance(),
  unidadTiempo: new ConstanteDTOStateInstance(),
  mediosRecepcion: new ConstanteDTOStateInstance(),
  tipologiaDocumental: new ConstanteDTOStateInstance(),
  tratamientoCortesia: new ConstanteDTOStateInstance(),
  tipoVia: new ConstanteDTOStateInstance(),
  prefijoCuadrante: new ConstanteDTOStateInstance(),
  bis: new ConstanteDTOStateInstance(),
  orientacion: new ConstanteDTOStateInstance(),
  tipoComplemento: new ConstanteDTOStateInstance(),
  actuaCalidad: new ConstanteDTOStateInstance(),
  causalDevolucion: new ConstanteDTOStateInstance(),
  soporteAnexo: new ConstanteDTOStateInstance(),
  modalidadCorreo: new ConstanteDTOStateInstance(),
  claseEnvio: new ConstanteDTOStateInstance(),
  motivoNoCreacionUD: new ConstanteDTOStateInstance(),
  tipoContacto: new ConstanteDTOStateInstance()
};

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {

  switch (action.type) {

    case Autocomplete.LOAD_SUCCESS: {
      const target = action.payload.key;
      const values = action.payload.data.constantes;
      const newValues = values.filter(data => !state[target].entities[data.codigo]);

      const newValuesIds = newValues.map(data => data.codigo);
      const newValuesEntities = newValues.reduce((entities: { [id: string]: ConstanteDTO }, value: ConstanteDTO) => {
        return Object.assign(entities, {
          [value.codigo]: value
        });
      }, {});

      const cloneState = Object.assign({}, state);
      cloneState[target] = {
        ids: [...state[target].ids, ...newValuesIds],
        entities: Object.assign({}, state[target].entities, newValuesEntities)
      };
      return cloneState;
    }

    case Autocomplete.LOAD_CAUSAL_DEVOLUCION: {
      const target = action.payload.key;
      const causalDevolucion: ConstanteDTO[] = [];

      causalDevolucion.push({
        id: 1,
        codigo: 'CI',
        nombre: 'Calidad Imagen'
      }, {
        id: 2,
        codigo: 'DI',
        nombre: 'Datos incorrectos'
      });

      const newValues = causalDevolucion.filter(data => !state[target].entities[data.codigo]);

      const newValuesIds = newValues.map(data => data.codigo);
      const newValuesEntities = newValues.reduce((entities: { [id: string]: ConstanteDTO }, value: ConstanteDTO) => {
        return Object.assign(entities, {
          [value.codigo]: value
        });
      }, {});
      const cloneState = Object.assign({}, state);
      cloneState[target] = {
        ids: [...state[target].ids, ...newValuesIds],
        entities: Object.assign({}, state[target].entities, newValuesEntities)
      };
      return cloneState;
    }

    case Autocomplete.FILTER: {
      const $target = action.payload.key;
      const query = action.payload.data;
      const cloneState = Object.assign({}, state);
      cloneState[$target] = tassign(cloneState[$target], {
        filter: query
      });
      return cloneState;
    }

    default:
      return state;
  }
}


