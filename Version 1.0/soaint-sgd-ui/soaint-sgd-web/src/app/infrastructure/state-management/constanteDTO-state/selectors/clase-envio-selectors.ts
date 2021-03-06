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

export const getClaseEnvioEntities = createSelector(rootPath, (state: State) => state.claseEnvio.entities);

export const getClaseEnvioIds = createSelector(rootPath, (state: State) => state.claseEnvio.ids);

export const getClaseEnvioArrayData = createSelector(getClaseEnvioEntities, getClaseEnvioIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});
