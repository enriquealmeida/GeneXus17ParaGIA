'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-aaee8a68.js');
const patch = require('./patch-b06e478a.js');

const defineCustomElements = (win, options) => {
  if (typeof window === 'undefined') return Promise.resolve();
  return patch.patchEsm().then(() => {
  return index.bootstrapLazy([["gx-dso-styles-editor.cjs",[[0,"gx-dso-styles-editor",{"monaco":[1032],"usePlatformConfiguration":[4,"use-platform-configuration"],"value":[1],"configuration":[16],"readonlymessage":[1025],"readonly":[1028],"dispose":[64],"getTextEditor":[64]},[[0,"editorValueChanged","editorValueChangedListener"]]]]]], options);
  });
};

exports.defineCustomElements = defineCustomElements;
