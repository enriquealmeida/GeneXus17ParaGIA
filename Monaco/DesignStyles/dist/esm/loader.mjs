import { b as bootstrapLazy } from './index-4449a5b3.js';
import { a as patchEsm } from './patch-a87348eb.js';

const defineCustomElements = (win, options) => {
  if (typeof window === 'undefined') return Promise.resolve();
  return patchEsm().then(() => {
  return bootstrapLazy([["gx-dso-styles-editor",[[0,"gx-dso-styles-editor",{"monaco":[1032],"usePlatformConfiguration":[4,"use-platform-configuration"],"value":[1],"configuration":[16],"readonlymessage":[1025],"readonly":[1028],"dispose":[64],"getTextEditor":[64]},[[0,"editorValueChanged","editorValueChangedListener"]]]]]], options);
  });
};

export { defineCustomElements };
