import { Component, EventEmitter, Input, OnChanges, Output, ViewChild } from "@angular/core";
import { Title } from '@angular/platform-browser';
import { AppContainer } from 'app/gx/base/app-container';
import { UriCacheService } from "app/gx/utils/uri-cache/uri-cache.service";


@Component({
  selector: 'gx-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss'],
})

export class FileUploadComponent implements OnChanges {

  @Input() fileSource = '';
  @Input() disabled = false;
  @Input() readonly = false;
  @Input() uploadService = null;

  @Output() onFileChanged: EventEmitter<string> = new EventEmitter<string>();

  @ViewChild('fileInput', { static: false }) file;

  showDialog = false;
  modalTitle: string;
  uploading = false;
  fileShowName = '';

  constructor(
    protected uriCacheService: UriCacheService,
    protected app: AppContainer,
    protected titleService: Title) {
  }

  ngOnChanges() {
    this.modalTitle = this.titleService.getTitle();
    this.fileShowName = this.fileSource.slice(this.fileSource.lastIndexOf("/") + 1);
  }

  triggerAction() {
    if (this.fileSource === '') {
      this.file.nativeElement.click();
    } else {
      this.showDialog = !this.showDialog;
    }
  }

  viewFile() {
    if (!this.showDialog) {
      this.app.open(this.fileSource);
    }
  }

  clearFileAction() {
    this.fileSource = '';
    this.uploading = false;
    this.onFileChanged.emit('');
  }

  async onFileSelected() {
    const files: { [key: string]: File } = this.file.nativeElement.files;
    for (let key in files) {
      if (!isNaN(parseInt(key))) {
        this.uploading = true;
        const objectId = await this.uploadFile(files[key]);
        this.onFileChanged.emit(objectId);
        this.uploading = false;
        this.showDialog = false;
        this.fileShowName = files[key].name;
        return;
      }
    }
  }

  async uploadFile(file: File): Promise<string> {
    if (this.uploadService) {
      const result = await this.uploadService(file);
      this.uriCacheService.store(result.object_id, file);
      return result.object_id;
    } else {
      const objectId = "gxupload:" + Math.floor(Math.random() * 100000000 + 1);
      this.uriCacheService.store(objectId, file);
      return objectId;
    }
  }

  closeAction() {
    this.showDialog = false;
  }

  translate(key: string) {
    return this.app.translate(key);
  }
}