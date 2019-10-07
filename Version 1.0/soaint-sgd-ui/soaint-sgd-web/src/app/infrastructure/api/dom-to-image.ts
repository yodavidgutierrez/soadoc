
import * as domtoimage from 'dom-to-image';
import * as html2canvas from 'html2canvas';
import * as detectBrowser from 'detect-browser';
import {Injectable} from '@angular/core';


export interface  IDomToImage {

  convertToBlob(node: HTMLElement ): Promise <Blob>;
}

 class DomToImage implements  IDomToImage {
  convertToBlob(node: HTMLElement ): Promise<Blob> {
    return domtoimage.toBlob(node);
  }
}

 class EdgeDomToImage implements IDomToImage {
  convertToBlob(node): Promise<Blob> {
    return node.then(canvas => {

      const dataUrl  = canvas.toDataURL( );

      const binStr = atob(dataUrl.split(',')[1]);

      const  len = binStr.length;
      const  arr = new Uint8Array(len);

      for (let i = 0; i < len; i++ ) {
        arr[i] = binStr.charCodeAt(i);
      }

      return new Blob([arr], { type: 'image/png'});
    });
  }
}

  class SafariDomToImage implements  IDomToImage {
    convertToBlob(node): Promise<Blob> {
      return domtoimage(node).then(canvas => {

        const dataUrl  = canvas.toDataURL( );

        const binStr = atob(dataUrl.split(',')[1]);

        const  len = binStr.length;
        const  arr = new Uint8Array(len);

        for (let i = 0; i < len; i++ ) {
          arr[i] = binStr.charCodeAt(i);
        }

        return new Blob([arr], { type: 'image/png' });
      });
    }
  }

export  class DomToImageFactory {

  readonly browser = detectBrowser.detect();
  getInstance(): IDomToImage {

    switch (this.browser.name) {
      case 'edge' :  return new EdgeDomToImage();
      case 'safari' : return new SafariDomToImage();
    }
    return new DomToImage();
  }
}

@Injectable()
export class DomToImageService {

  constructor(private domtoImage: IDomToImage) {
  }

  convertToBlob(node: HTMLElement ): Promise<Blob> {
    return this.domtoImage.convertToBlob(node);
  }
}

export const domToImageProvider = {
  provide: DomToImageService,
  useFactory() {
    const factory = new DomToImageFactory();
    return new DomToImageService(factory.getInstance());
  }
};

