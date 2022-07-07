if (!window.checkReadyGXtest) {
    window.checkReadyGXtest = function () {

        var isReady = typeof document !== 'undefined' && document.readyState === 'complete';

        if (!isReady) {
            return false;
        }

        if (typeof gx === 'undefined') {
            // If it is not a gx page, then checking document.readyState is the only thing that can be checked
            return isReady;
        }

        if (!gx.isReadyUI) {
            // for GX versions previous to v17u1 the function is not defined
            gx.isReadyUI = function () {
                var gxObject = gx;
                // When there is a popup opened the waiting is done against the last opened frame (most in the front)
                if (typeof gx.popup !== 'undefined' && typeof gx.popup.getPopup !== 'undefined' && gx.popup.getPopup() !== null) {

                    // once we spot there is a popup opened the result of ready checking must be resolved inside this if because
                    // if not, the result of the function will be always false because root frame is set as not ready due is waiting for an opened popup

                    var p = gx.popup.getPopup();
                    while (p !== null) {

                        if (p.parentPopup === null && p.frameWindow && p.frameWindow.gx) {
                            // it is the most in the front popup, so check its ready state against its gx object instead of main gx
                            gxObject = p.frameWindow.gx;
                        }
                        p = p.parentPopup;
                    }
                }

                if (typeof gxObject.evt !== 'undefined' && typeof gxObject.evt.userReady !== 'undefined') {

                    if (!gxObject.evt.userReady) {
                        return false;
                    }
                }

                if (typeof gxObject.ajax !== 'undefined' && typeof gxObject.ajax.isFormEnabled !== 'undefined') {

                    if (!gxObject.ajax.isFormEnabled()) {
                        return false;
                    }
                }

                if (typeof gxObject.isInputEnabled !== 'undefined') {

                    if (!gxObject.isInputEnabled()) {
                        return false;
                    }
                }

                return true;
            };
        }

        return gx.isReadyUI();
    };
};
return window.checkReadyGXtest();
