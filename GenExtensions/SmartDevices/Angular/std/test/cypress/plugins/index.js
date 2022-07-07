const {
    addMatchImageSnapshotPlugin,
  } = require('cypress-img-snapshot/dist/plugin');
  
  const { lighthouse, prepareAudit } = require('cypress-audit');

  module.exports = (on, config) => {
    // cypress-lighthouse setup
    on('before:browser:launch', (browser = {}, launchOptions) => {
      prepareAudit(launchOptions);
    });
  
    on('task', {
      lighthouse: lighthouse(),
    });

    // cypress-image-snapshot setup
    addMatchImageSnapshotPlugin(on, config);
  };