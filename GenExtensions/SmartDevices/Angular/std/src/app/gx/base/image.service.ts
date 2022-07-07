import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Settings } from "../../app.settings";

@Injectable({
  providedIn: "root"
})
export class ImageService {
  constructor(private http: HttpClient) { }

  images: { [id: string]: {location: string, options: ImageOption[]}[] } = {};
  loadedImages: { [id: string]: boolean } = {};

  async load(language: string, theme: string) {
    await this.loadImages(language, theme);
  }

  async loadImages(language: string, theme: string) {
    if (!this.loadedImages[language + theme]) {
      try {
        let response = await this.http
          // TODO: Load images from a specific theme/language definition file
          //.get(`assets/images.${theme}.${language}.json`)
          .get(`assets/images.json`)
          .toPromise();
        const data = response as ImagesData;
        data.images.forEach(image => {

          if (image.colorScheme) {
            const data = {
              location: image.location,
              options: image.options
            };
            const key = this.resolveImageKey(image.name, image.lang, image.theme, image.colorScheme);

            if (this.images[key]) {
              let addedImage = this.images[key].find(imageOption => JSON.stringify(imageOption.options) === JSON.stringify((image.options)));
              if (addedImage) {
                addedImage.location = image.location;
              } else {
                this.images[key].push(data);
              }
            } else {
              this.images[key] = [data];
            }
          } else {
            const dataLight = {
              location: image.location,
              options: image.options
            };
            const keyLight = this.resolveImageKey(image.name, image.lang, image.theme, "light");
            if (this.images[keyLight]) {
              this.images[keyLight].push(dataLight);
            } else {
              this.images[keyLight] = [dataLight];
            }

            const dataDark = {
              location: image.location,
              options: image.options
            };
            const keyDark = this.resolveImageKey(image.name, image.lang, image.theme, "dark");
            if (this.images[keyDark]) {
              this.images[keyDark].push(dataDark);
            } else {
              this.images[keyDark] = [dataDark];
            }
          }
        });
        this.loadedImages[language + theme] = true;
      } catch (e) {
        console.log(
          `Could not load images definition for ${language}/${theme}`,
          e
        );
      }
    }
  }

  getImageSource(name: string, language: string, theme: string, colorScheme: string, options: ImageOption[]): string {
    const imageUrl = this.getImage(name, language, theme, colorScheme, options);
    if (imageUrl === "") {
      if (name.startsWith("https://") || name.startsWith("http://") || name.startsWith("/")) {
        return name;
      } else {
        return "";
      }
    }
    return "assets/" + imageUrl;
  }

  private getImage(name: string, language: string, theme: string, colorScheme: string, options: ImageOption[]): string {
    const imageOptions = this.images[this.resolveImageKey(name, language, theme, colorScheme)];

    if (imageOptions === null || imageOptions === undefined) {
      return "";
    } else {
      let location = "";
      let specificity = -1;

      for(let i=0; i<imageOptions.length; i++) {
        let imageOption = imageOptions[i];
        let match = imageOption.options.every(entry => options.some(option => option.name == entry.name && option.value == entry.value));

        if (match && imageOption.options.length > specificity) {
          location = imageOption.location;
          specificity = imageOption.options.length;
        }
      }

      return location;
    }
  }

  private resolveImageKey(name: string, lang: string, theme: string, colorScheme: string) {
    return `${name.toLowerCase()}_${lang.toLowerCase()}_${theme.toLowerCase()}_${colorScheme.toLowerCase()}`;
  }
}

export class ImagesData {
  images: ImageData[];
}

export class ImageData {
  name: string;
  location: string;
  theme: string;
  lang: string;
  colorScheme: string;
  options: ImageOption[];
}

export class ImageOption {
  name: string;
  value: string;
}
