import {
  Component,
  AfterViewInit,
  ElementRef,
  Renderer,
  ViewChild,
  OnInit,
  OnDestroy,
  HostListener,
  ChangeDetectionStrategy
} from '@angular/core';

import {MenuOrientation} from './models/admin-layout.model';
import {Observable} from 'rxjs/Observable';
import {AdminLayoutSandbox} from './redux-state/admin-layout-sandbox';
import {FOR_ROLE_OPTIONS, GROUPS, MENU_OPTIONS, RADICADORA_ROLES} from '../../../../shared/menu-options/menu-options';
import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isNullOrUndefined} from "util";
import {MenuItem} from "primeng/primeng";
import {Subscription} from "rxjs/Subscription";
import {combineLatest} from "rxjs/observable/combineLatest";
import { LoginSandbox } from 'app/ui/page-components/login/__login.include';

declare var jQuery: any;

enum LayoutResponsive {
  MOBILE,
  TABLET,
  DESKTOP
}

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminLayoutComponent implements AfterViewInit, OnInit, OnDestroy {

  processOptions: Observable<any[]>;

  layoutWidth$: Observable<number>;

  menuOptions: MenuItem[];

  layoutCompact = false;

  layoutMode: MenuOrientation = MenuOrientation.STATIC;

  darkMenu = false;

  profileMode = 'inline';

  rotateMenuButton: boolean;

  topbarMenuActive: boolean;

  overlayMenuActive: boolean;

  staticMenuDesktopInactive: boolean;

  staticMenuMobileActive: boolean;

  layoutContainer: HTMLDivElement;

  layoutMenuScroller: HTMLDivElement;

  menuClick: boolean;

  topbarItemClick: boolean;

  activeTopbarItem: any;

  documentClickListener: Function;

  resetMenu: boolean;

  menuIsVisible:boolean = true;

  funcionarioLog;

  @ViewChild('layoutContainer') layourContainerViewChild: ElementRef;

  @ViewChild('layoutMenuScroller') layoutMenuScrollerViewChild: ElementRef;

  isAuthenticated$: Observable<boolean>;

  layoutResponsive: LayoutResponsive;

  funcionarioDependenciaSuggestions$: Observable<ConstanteDTO[]>;
  funcionarioDependenciaSelected$: Observable<ConstanteDTO>;
  usernameAuth$: Observable<string>;


  subscriptions:Subscription[] = [];

  constructor(private _sandboxL: LoginSandbox,private _sandbox: AdminLayoutSandbox, public renderer: Renderer,private _store:Store<RootState>) {
  }

  ngOnInit(): void {
    // var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
    // var eventer = window[eventMethod];
    // var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";
    // eventer(messageEvent, (e) => { 
    //     if (String(e.data).includes('soaint2')) {
    //   setTimeout(() => {
      
    //     let data = String(e.data).replace('soaint2','');
    //     var bt = JSON.parse(atob(data));
    //      // set payload
    // const payload = {
    //   username: bt['username'],
    //   password: bt['password']
    // };

    // this._sandboxL.loginDispatch(payload);
 
    //   }, 100);
    //     }
    // }, false);

    combineLatest( this._store.select(getAuthenticatedFuncionario),this._store.select(getSelectedDependencyGroupFuncionario))
  .subscribe( ([funcionario ,dependencia])=> {

     if(isNullOrUndefined(funcionario))
       return;

     this.funcionarioLog = funcionario.loginName;

     this.menuIsVisible = !isNullOrUndefined(funcionario.dependencias) && funcionario.dependencias.length > 0;

     this.menuOptions = MENU_OPTIONS;

      if(isNullOrUndefined(funcionario.roles))
       return;
       const addedGroup = [];
            funcionario.roles.forEach( rol => {
       
              if(!isNullOrUndefined(FOR_ROLE_OPTIONS[rol.rol])){
                if(!RADICADORA_ROLES.includes(rol.rol) || (!isNullOrUndefined(dependencia) &&  dependencia.radicadora)){
                  this.menuOptions =   this.menuOptions.concat(FOR_ROLE_OPTIONS[rol.rol]);
                }
              }
                Object.keys(GROUPS).forEach( group =>{
                  if(!addedGroup.includes(group) && GROUPS[group].includes(rol.rol) && !isNullOrUndefined(FOR_ROLE_OPTIONS[group])){
                    this.menuOptions = this.menuOptions.concat(FOR_ROLE_OPTIONS[group]);
                    addedGroup.push(group);
                  }
                });
       
           })
       
          });
 

    this.processOptions = this._sandbox.selectorDeployedProcess();
    this.isAuthenticated$ = this._sandbox.selectorIsAutenticated();
    this.usernameAuth$ = this._sandbox.selectorUsername();
    this.layoutWidth$ = this._sandbox.selectorWindowWidth();
    this.funcionarioDependenciaSuggestions$ = this._sandbox.selectorFuncionarioAuthDependenciasSuggestions();
    this.funcionarioDependenciaSelected$ = this._sandbox.selectorFuncionarioAuthDependenciaSelected();

    this.layoutWidth$.subscribe(width => {
      if (width <= 640) {
        return this.layoutResponsive = LayoutResponsive.MOBILE
      }

      if (width <= 1024 && width > 640) {
        return this.layoutResponsive = LayoutResponsive.TABLET
      }

      if (width >= 1024) {
        return this.layoutResponsive = LayoutResponsive.DESKTOP
      }
    });

    this.hideMenu();

    this.isAuthenticated$.subscribe(isAuthenticated => {
      // console.info(isAuthenticated);
      if (isAuthenticated) {
        this._sandbox.dispatchMenuOptionsLoad();
        this.displayMenu();
      } else {
        this.hideMenu();
      }
    });
  }

  ngAfterViewInit() {
    this.layoutContainer = <HTMLDivElement>this.layourContainerViewChild.nativeElement;
    this.layoutMenuScroller = <HTMLDivElement>this.layoutMenuScrollerViewChild.nativeElement;

    // hides the horizontal submenus or top menu if outside is clicked
    this.documentClickListener = this.renderer.listenGlobal('body', 'click', (event) => {
      if (!this.topbarItemClick) {
        this.activeTopbarItem = null;
        this.topbarMenuActive = false;
      }

      if (!this.menuClick && this.isHorizontal()) {
        this.resetMenu = true;
      }

      this.topbarItemClick = false;
      this.menuClick = false;
    });

    setTimeout(() => {
      jQuery(this.layoutMenuScroller).nanoScroller({flash: true});
    }, 10);
  }

  onMenuButtonClick(event) {
    this.rotateMenuButton = !this.rotateMenuButton;
    this.topbarMenuActive = false;

    if (this.layoutMode === MenuOrientation.OVERLAY) {
      this.overlayMenuActive = !this.overlayMenuActive;
    } else {
      if (this.isDesktop()) {

        this.staticMenuDesktopInactive = !this.staticMenuDesktopInactive;
      } else {
        this.staticMenuMobileActive = !this.staticMenuMobileActive;
      }
    }

    event.preventDefault();
  }

  onMenuClick($event) {
    this.menuClick = true;
    this.resetMenu = false;

    if (!this.isHorizontal()) {
      setTimeout(() => {
        jQuery(this.layoutMenuScroller).nanoScroller();
      }, 500);
    }
  }

  onTopbarMenuButtonClick(event) {
    this.topbarItemClick = true;
    this.topbarMenuActive = !this.topbarMenuActive;

    if (this.overlayMenuActive || this.staticMenuMobileActive) {
      this.rotateMenuButton = false;
      this.overlayMenuActive = false;
      this.staticMenuMobileActive = false;
    }

    event.preventDefault();
  }

  onTopbarItemClick(event, item) {
    this.topbarItemClick = true;

    if (this.activeTopbarItem === item) {
      this.activeTopbarItem = null;
    } else {
      this.activeTopbarItem = item;
    }
    event.preventDefault();
  }

  onFuncionarioDependenciaChange(dependenciaGrupo) {
    this._sandbox.dispatchFuncionarioAuthDependenciaSelected(dependenciaGrupo);
  }

  signOff(): void {
    this._sandbox.dispatchLogoutUser();
  }

  public hideMenu(): void {
    this.staticMenuDesktopInactive = true;
    this.staticMenuMobileActive = false;
  }

  public displayMenu(): void {
    this.staticMenuDesktopInactive = false;
    this.staticMenuMobileActive = true;
  }


  isTablet() {
    return this.layoutResponsive === LayoutResponsive.TABLET;
  }

  isDesktop() {
    return this.layoutResponsive === LayoutResponsive.DESKTOP;
  }

  isMobile() {
    return this.layoutResponsive === LayoutResponsive.MOBILE;
  }

  isOverlay() {
    return this.layoutMode === MenuOrientation.OVERLAY;
  }

  isHorizontal() {
    return this.layoutMode === MenuOrientation.HORIZONTAL;
  }

  changeToStaticMenu() {
    this.layoutMode = MenuOrientation.STATIC;
  }

  changeToOverlayMenu() {
    this.layoutMode = MenuOrientation.OVERLAY;
  }

  changeToHorizontalMenu() {
    this.layoutMode = MenuOrientation.HORIZONTAL;
  }

  triggerProccess(param1, param2) {
    console.log(param1, param2, this);
  }

  ngOnDestroy() {
    if (this.documentClickListener) {
      this.documentClickListener();
    }

    jQuery(this.layoutMenuScroller).nanoScroller({flash: true});

    this.subscriptions.forEach(s => s.unsubscribe());
  }

  @HostListener('window:resize', ['$event'])
  onResize($event) {
    this._sandbox.dispatchWindowResize({width: $event.target.innerWidth, height: $event.target.innerHeight});
  }

  routeToSecurityRole(): void {
    console.log('Log....');
    this._sandbox.dispatchRoutingSecurityRole();
  }

}
