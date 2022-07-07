import { Component, EventEmitter, Input, Output, ViewChild } from "@angular/core";

import { AppContainer } from "app/gx/base/app-container";
import { Title } from "@angular/platform-browser";
import { UriCacheService } from "app/gx/utils/uri-cache/uri-cache.service";

@Component({
  selector: "gx-image-upload",
  templateUrl: "./image-upload.component.html",
  styleUrls: ["./image-upload.component.scss"],
})
export class ImageUploadComponent {
  @Input() alt = "";
  @Input() autoGrow = true;
  @Input() cssClass: string = null;
  @Input() disabled = false;
  @Input() invisibleMode: "collapse" | "keep-space" = "collapse";
  @Input() lazyLoad = true;
  @Input() lowResolutionSrc = "";
  @Input() scaleType: "contain" | "cover" | "fill" | "none" | "tile";
  @Input() src = "";
  @Input() highlightable = false;
  @Input() readonly = false;
  @Input() modalTitle = null;
  @Input() changeButtonText = "Change image...";
  @Input() removeButtonText = "Remove image";
  @Input() cancelButtonText = "GXM_cancel";
  @Input() uploadService = null;

  @Output() click: EventEmitter<MouseEvent> = new EventEmitter();
  @Output() onImageChanged: EventEmitter<string> = new EventEmitter<string>();

  @ViewChild("imageUpload", { static: false }) imageUpload;

  state: "readyToUse" | "fileReadyToUpload" | "uploadingFile" = "readyToUse";

  constructor(
    protected uriCacheService: UriCacheService,
    protected app: AppContainer,
    protected titleService: Title
  ) {}

  handleImageChange(event: CustomEvent) {
    // This stopPropagation() allows to not call the external event, defined
    // when using the Angular's control
    event.stopPropagation();

    // The image was removed
    if (event.detail == null) {
      this.src = null;
      this.alt = "";
      this.onImageChanged.emit("");
    }
    // The image was updated
    else {
      const imageFile: File = event.detail as File;
      this.fileSelectedAction(imageFile);
    }
  }

  clickImageAction(event) {
    // This stopPropagation() allows to not call the external event, defined
    // when using the Angular's control
    event.stopPropagation();

    this.click.emit(event);
  }

  async fileSelectedAction(file: File) {
    this.state = "uploadingFile";
    const objectId = await this.onFileSelected(file);
    if (objectId) {
      await this.updateImage(file, objectId);
    }
    this.state = "readyToUse";
    return;
  }

  async onFileSelected(file: File): Promise<string> {
    let result = await this.uploadService(file);
    return result.object_id;
  }

  translate(key: string) {
    return this.app.translate(key);
  }

  updateImage(file: File, objectId: string): Promise<void> {
    return new Promise((complete) => {
      if (file) {
        const gxImageUpload = this.imageUpload
          .nativeElement as HTMLGxImagePickerElement;
        const fileURL = this.uriCacheService.store(objectId, file);
        this.src = fileURL;
        this.alt = gxImageUpload.alt;
        this.onImageChanged.emit(objectId);
        complete();
      }
    });
  }
}
