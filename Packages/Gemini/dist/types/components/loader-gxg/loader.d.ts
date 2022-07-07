export declare class GxgLodaer {
    /**
     * The text you want to show under the loader (optional)
     */
    text: string;
    /**
     * The prescence of this attribute shows the loader
     */
    show: boolean;
    /**
     * The z-index positive value you want for the loader when visible (default: 100)
     */
    visibleZIndex: string;
    layerOpacity100: boolean;
    squaresOpacity100: boolean;
    textOpacity100: boolean;
    sendLayerBack: boolean;
    showHandler(): void;
    render(): any;
}
