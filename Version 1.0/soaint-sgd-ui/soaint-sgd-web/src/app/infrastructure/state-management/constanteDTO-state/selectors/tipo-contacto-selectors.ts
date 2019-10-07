import {State} from '../constanteDTO-reducers';
import {createSelector} from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';


const rootPath = (state: rootStore.State) => state.constantes;

/**
 * Because the data structure is defined within the reducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 */

export const getTipoContactoEntities = createSelector(rootPath, (state: State) => state.tipoContacto.entities);

export const getTipoContactoIds = createSelector(rootPath, (state: State) => state.tipoContacto.ids);

export const getTipoContactoSelectedId = createSelector(rootPath, (state: State) => state.tipoContacto.selectedId);

// Get Tipo destinatario principal id=> 52


export const getTipoContactoArrayData = createSelector(getTipoContactoEntities, getTipoContactoIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});
