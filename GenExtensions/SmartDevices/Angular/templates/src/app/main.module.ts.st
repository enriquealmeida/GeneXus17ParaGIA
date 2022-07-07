import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { environment } from "./../environments/environment";
import { ServiceWorkerModule } from "@angular/service-worker";

import { CommonModule as GxCommonModule } from "./common.module";
import { VirtualScrollerModule } from '@genexus/ngx-virtual-scroller';

$data.PanelsMain: { obj | import { $obj.FullName$Component \} from "app/$obj.ProjectPath$/$gx.ToLower.(obj.FileName)$.component" }; separator=";\r\n"$;
$if(data.UserControls)$
import { UsercontrolsModule } from "./usercontrols.module";
$endif$

@NgModule({
  imports: [
    CommonModule, 
    FormsModule,
    GxCommonModule,
    VirtualScrollerModule,
    ServiceWorkerModule.register("ngsw-worker.js", {
      enabled: environment.production
    })
    $if(data.UserControls)$
    , UsercontrolsModule
    $endif$
  ],
  declarations: [
    $data.PanelsMain: { obj | $obj.FullName$Component }; separator = ",\r\n"$  
  ],
  exports: [
    $data.PanelsMain: { obj | $obj.FullName$Component }; separator = ",\r\n"$  
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MainModule {
}
