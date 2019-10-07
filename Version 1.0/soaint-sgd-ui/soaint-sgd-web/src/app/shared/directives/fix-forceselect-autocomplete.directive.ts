import {Directive, Host, Optional, Self} from "@angular/core";
import {AutoComplete} from "primeng/primeng";

@Directive({
  selector: 'p-autoComplete[fixForceSelect]'
})
export  class FixForceselectAutocompleteDirective{

  constructor(
    @Host() @Self() @Optional() public autocomplete : AutoComplete) {
    // Now you can access specific instance members of host directive
   // let app = (<any>hostSel)._app;
    // also you can override specific methods from original host directive so that this specific instance uses your method rather than their original methods.
    autocomplete.selectItem = (option) => {

      if (autocomplete.multiple) {
        autocomplete.multiInputEL.nativeElement.value = '';
        autocomplete.value = autocomplete.value || [];
        if (!autocomplete.isSelected(option)) {
          autocomplete.value = autocomplete.value.concat([option]);
          autocomplete.onModelChange(autocomplete.value);
        }
      }
      else {
        autocomplete.inputEL.nativeElement.value = autocomplete.field ? autocomplete.objectUtils.resolveFieldData(option, autocomplete.field) || '' : option;
        autocomplete.value = option;
        autocomplete.onModelChange(autocomplete.value);
      }
      autocomplete.onSelect.emit(option);
      if(autocomplete.focus)
      autocomplete.focusInput();
    }
  }

}
