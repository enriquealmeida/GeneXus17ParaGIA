var selectorConfig = { timeout: 1000 };

export function gxTestSuite(testSuite) {
  describe(testSuite.description, () => {

    beforeEach(() => {
      cy.intercept("**/rest/**").as("gxRestSvc");
      cy.visit(testSuite.url);
    });

    for (const testCase of testSuite.testCases) {
      it(testCase.description, () => {
        if (testSuite.connectsRestService) {
          cy.wait(['@gxRestSvc']);
        }
        cy.window().its('getAllAngularRootElements' as any).should('exist');
        cy.wait(1000);
        cy.hideNavbar();

        for (const action of testCase.instructions) {
          doAction(action);
        }
      });
    }
  });
}

function doAction(action) {
  if (action.type === 'button_click') {
    // BUTTON CLICK
    var actionTarget = cy.get('gx-button[data-test-id="' + action.id + '"]');
    actionTarget.click();
  }
  if (action.type === 'tabpage_click') {
    // BUTTON CLICK
    var actionTarget = cy.get('gx-tab-caption[data-test-id="' + action.id + '"]');
    actionTarget.click();
  }
  else if (action.type === "cell_assert") {
    // ASSERT ON CELLS
    for (const cellId of action.cells) {
      var el = cy.get('gx-table-cell[data-test-id="' + cellId + '"]', selectorConfig);
      el.matchImageSnapshot(action.description + " : " + cellId);
    }
  }
}
