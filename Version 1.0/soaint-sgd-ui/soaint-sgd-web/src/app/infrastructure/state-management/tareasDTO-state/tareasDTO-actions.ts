import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
  FILTER: type('[TareaDTO] FilterAction'),
  FILTER_COMPLETE: type('[TareaDTO] FilterCompleteAction'),
  LOAD: type('[TareaDTO] LoadAction'),
  LOAD_SUCCESS: type('[TareaDTO] LoadSuccessAction'),
  LOAD_FAIL: type('[TareaDTO] LoadFailAction'),
  START_TASK: type('[TareaDTO] StartTaskAction'),
  RESERVE_TASK: type('[TareaDTO] ReserveTaskAction'),
  START_INPROGRESS_TASK: type('[TareaDTO] StartInProgressTaskAction'),
  START_TASK_SUCCESS: type('[TareaDTO] StartTaskSuccessAction'),
  START_TASK_FAIL: type('[TareaDTO] StartTaskFailAction'),
  COMPLETE_TASK: type('[TareaDTO] CompleteTaskAction'),
  COMPLETE_TASK_SUCCESS: type('[TareaDTO] CompleteTaskSuccessAction'),
  COMPLETE_TASK_FAIL: type('[TareaDTO] CompleteTaskFailAction'),
  COMPLETE_BACK_TASK: type('[TareaDTO] CompleteBackTaskAction'),
  COMPLETE_BACK_TASK_SUCCESS: type('[TareaDTO] CompleteBackTaskSuccessAction'),
  COMPLETE_BACK_TASK_FAIL: type('[TareaDTO] CompleteBackTaskFailAction'),
  ABORT_TASK: type('[TareaDTO] AbortTaskAction'),
  ABORT_TASK_SUCCESS: type('[TareaDTO] AbortTaskSuccessAction'),
  ABORT_TASK_FAIL: type('[TareaDTO] AbortTaskFailAction'),
  CANCEL_TASK: type('[TareaDTO] CancelTaskAction'),
  CANCEL_TASK_SUCCESS: type('[TareaDTO] CancelTaskSuccessAction'),
  CANCEL_TASK_FAIL: type('[TareaDTO] CancelTaskFailAction'),
  LOCK_ACTIVE_TASK: type('[TareaDTO] LockActiveTaskAction'),
  UNLOCK_ACTIVE_TASK: type('[TareaDTO] UnlockActiveTaskAction'),
  SCHEDULE_NEXT_TASK: type('[TareaDTO] ScheduleNextTaskAction'),
  CONTINUE_WITH_NEXT_TASK: type('[TareaDTO] ContinueWithNextTaskAction'),
  GET_TASK_STATS: type('[TareaDTO] GetTaskStatsAction'),
  GET_TASK_STATS_SUCCESS: type('[TareaDTO] GetTaskStatsSuccessAction'),
  RESET_TASK: type('[TareaDTO] ResetTaskAction'),
  CLEAR_TASKS: type('[TAREADTO] ClearTasksAction')

};

export class FilterAction implements Action {
  type = ActionTypes.FILTER;

  constructor(public payload?: any) {
  }
}

export class FilterCompleteAction implements Action {
  type = ActionTypes.FILTER_COMPLETE;

  constructor(public payload?: any) {
  }
}

export class LoadAction implements Action {
  type = ActionTypes.LOAD;

  constructor(public payload?: any) {
  }
}

export class LoadSuccessAction implements Action {
  type = ActionTypes.LOAD_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class LoadFailAction implements Action {
  type = ActionTypes.LOAD_FAIL;

  constructor(public payload?: any) {
  }
}

export class StartTaskAction implements Action {
  type = ActionTypes.START_TASK;

  constructor(public payload?: any) {
  }
}

export class ReserveTaskAction implements Action {
  type = ActionTypes.RESERVE_TASK;

  constructor(public payload?: any) {
  }
}

export class StartInProgressTaskAction implements Action {
  type = ActionTypes.START_INPROGRESS_TASK;

  constructor(public payload?: any) {
  }
}

export class StartTaskSuccessAction implements Action {
  type = ActionTypes.START_TASK_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class StartTaskFailAction implements Action {
  type = ActionTypes.START_TASK_FAIL;

  constructor(public payload?: any) {
  }
}

export class CompleteTaskAction implements Action {
  type = ActionTypes.COMPLETE_TASK;

  constructor(public payload?: any) {
  }
}

export class CompleteTaskSuccessAction implements Action {
  type = ActionTypes.COMPLETE_TASK_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class CompleteTaskFailAction implements Action {
  type = ActionTypes.COMPLETE_TASK_FAIL;

  constructor(public payload?: any) {
  }
}

export class CompleteBackTaskAction implements Action {
  type = ActionTypes.COMPLETE_BACK_TASK;

  constructor(public payload?: any) {
  }
}

export class CompleteBackTaskSuccessAction implements Action {
  type = ActionTypes.COMPLETE_BACK_TASK_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class CompleteBackTaskFailAction implements Action {
  type = ActionTypes.COMPLETE_BACK_TASK_FAIL;

  constructor(public payload?: any) {
  }
}

export class AbortTaskAction implements Action {
  type = ActionTypes.ABORT_TASK;

  constructor(public payload?: any) {
  }
}

export class AbortTaskSuccessAction implements Action {
  type = ActionTypes.ABORT_TASK_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class AbortTaskFailAction implements Action {
  type = ActionTypes.ABORT_TASK_FAIL;

  constructor(public payload?: any) {
  }
}

export class CancelTaskAction implements Action {
  type = ActionTypes.CANCEL_TASK;

  constructor(public payload?: any) {
  }
}

export class CancelTaskSuccessAction implements Action {
  type = ActionTypes.CANCEL_TASK_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class CancelTaskFailAction implements Action {
  type = ActionTypes.CANCEL_TASK_FAIL;

  constructor(public payload?: any) {
  }
}

export class UnlockActiveTaskAction implements Action {
  type = ActionTypes.UNLOCK_ACTIVE_TASK;

  constructor(public payload?: any) {
  }
}

export class LockActiveTaskAction implements Action {
  type = ActionTypes.LOCK_ACTIVE_TASK;

  constructor(public payload?: any) {
  }
}

export class ContinueWithNextTaskAction implements Action {
  type = ActionTypes.CONTINUE_WITH_NEXT_TASK;

  constructor(public payload?: any) {
  }
}

export class ScheduleNextTaskAction implements Action {
  type = ActionTypes.SCHEDULE_NEXT_TASK;

  constructor(public payload?: any) {
  }
}

export class GetTaskStatsAction implements Action {
  type = ActionTypes.GET_TASK_STATS;

  constructor(public payload?: any) {
  }
}

export class GetTaskStatsSuccessAction implements Action {
  type = ActionTypes.GET_TASK_STATS_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class ResetTaskAction implements Action {
  type = ActionTypes.RESET_TASK;
  constructor(public payload?: any) { }
}

export class ClearTasksAction implements Action {
  type = ActionTypes.CLEAR_TASKS;
  constructor(public payload?: any) { }
}

export type Actions =
  FilterAction |
  LoadAction |
  LoadSuccessAction |
  LoadFailAction |
  StartTaskAction |
  StartTaskSuccessAction |
  StartTaskFailAction |
  CompleteTaskAction |
  CompleteTaskSuccessAction |
  CompleteTaskFailAction |
  AbortTaskAction |
  AbortTaskSuccessAction |
  AbortTaskFailAction |
  CancelTaskAction |
  CancelTaskSuccessAction |
  CancelTaskFailAction |
  UnlockActiveTaskAction |
  LockActiveTaskAction  |
  ContinueWithNextTaskAction |
  ScheduleNextTaskAction |
  GetTaskStatsAction |
  GetTaskStatsSuccessAction |
  ResetTaskAction|
  ClearTasksAction  |
  FilterCompleteAction
  ;


