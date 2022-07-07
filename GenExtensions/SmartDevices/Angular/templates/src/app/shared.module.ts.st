import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";

import { CommonModule as GxCommonModule } from "./common.module";
import { MainModule } from "./main.module";
$if(data.UserControls)$
import { UsercontrolsModule } from "./usercontrols.module";
$endif$

import { SafePipe } from "app/gx/ui/controls/safe-pipe/safe-pipe.component";
$data.PanelsNotEntryPoint: { obj | import { $obj.FullName$Component \} from "app/$obj.ProjectPath$/$gx.ToLower.(obj.FileName)$.component" }; separator=";\r\n"$;

import { SharedRoutingModule, moduleRoutes } from "app/shared-routing.module";

import { VirtualScrollerModule } from '@genexus/ngx-virtual-scroller';

@NgModule({
  imports: [
    CommonModule, 
    FormsModule, 
    SharedRoutingModule, 
    VirtualScrollerModule,
    GxCommonModule,
    MainModule
    $if(data.UserControls)$
    , UsercontrolsModule
    $endif$
  ],
  declarations: [
    SafePipe
    $data.PanelsNotEntryPoint: { obj |, $obj.FullName$Component }; separator = "\r\n"$
  ],
  bootstrap: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SharedModule {
  static routes = moduleRoutes;
}
