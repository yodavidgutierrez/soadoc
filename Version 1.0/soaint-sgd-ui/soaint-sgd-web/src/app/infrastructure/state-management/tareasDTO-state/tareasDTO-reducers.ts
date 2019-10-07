import {ActionTypes, Actions} from './tareasDTO-actions';
import {tassign} from 'tassign';
import {TareaDTO} from 'app/domain/tareaDTO';
import {LoadNextTaskPayload} from '../../../shared/interfaces/start-process-payload,interface';
import {loadDataReducer} from '../../redux-store/redux-util';
import {EventEmitter} from "@angular/core";


export interface State {
  ids: string[];
  entities: { [idTarea: string]: TareaDTO };
  stats: {name: string, value: number }[];
  activeTask: TareaDTO;
  nextTask: LoadNextTaskPayload;
  openTask: boolean;
  totalRecords?: number;
}

const initialState: State = {
  ids: [],
  entities: {},
  stats: [],
  activeTask: null,
  nextTask: null,
  openTask: false,
  totalRecords: 0
};

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */

export const  afterTaskComplete:EventEmitter<any> = new EventEmitter;


export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case ActionTypes.FILTER_COMPLETE:
    case ActionTypes.LOAD_SUCCESS: {
      const s = loadDataReducer(action, state, action.payload.tareas, 'idTarea');
      return tassign(s, {totalRecords: action.payload.cantidad});
    }

    case ActionTypes.LOCK_ACTIVE_TASK: {
      return tassign(state, {
        activeTask: action.payload, // task
        nextTask: null,
      });
    }

    case ActionTypes.UNLOCK_ACTIVE_TASK: {
      return tassign(state, {
        activeTask: null,
        nextTask: null
      });
    }

    case ActionTypes.SCHEDULE_NEXT_TASK: {
      const nextTask = action.payload;
      return tassign(state, {
        nextTask: nextTask
      });
    }

    case ActionTypes.START_TASK_SUCCESS: {
      const task = action.payload;
      const cloneEntities = tassign({}, state.entities);
      cloneEntities[task.idTarea] = task;

      return tassign(state, {
        entities: cloneEntities,
        activeTask: task,
        openTask: true
      });

    }

    case ActionTypes.GET_TASK_STATS_SUCCESS: {
      const payload = action.payload;
      if(!Array.isArray(payload)) {
        return state;
      }
      let stats = [];
      payload.forEach(stat => {
        stats.push({ name: stat.status, value: stat.cantidad});
      });

      return tassign(state, {
        stats: stats
      });
    }

    case ActionTypes.COMPLETE_TASK_SUCCESS: {

      const task = action.payload;
      const cloneEntities = tassign({}, state.entities);
      delete cloneEntities[state.activeTask.idTarea];

        afterTaskComplete.emit();

      return tassign(state, {
        ids: state.ids.filter(value => value !== state.activeTask.idTarea),
        entities: cloneEntities,
        activeTask: null,
      });
    }
    case ActionTypes.RESET_TASK : {
      return tassign(state, {
        openTask: false
      });
    }

    case ActionTypes.CLEAR_TASKS : {

      return tassign(state, initialState);
    }

    default:
      return state;
  }
}


