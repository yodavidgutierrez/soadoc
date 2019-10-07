import {LoginSandbox, AuthenticatedGuard} from './login/__login.include';
import {TareaDtoGuard} from '../../infrastructure/state-management/tareasDTO-state/tareasDTO-guard';
import {BpmmanagerGuard} from "../../shared/guards/bpmmanager.guard";


export const PAGE_COMPONENTS_PROVIDERS = [
  LoginSandbox,
  AuthenticatedGuard,
  TareaDtoGuard,
  BpmmanagerGuard
];
