
function Switch($) {
    this.Value;
    this.CheckedValue;
    this.UncheckedValue;
    this.Checked;
    this.Visible;
    this.Enabled;
	this.OnText;
	this.OffText
	
    var $container, baseId, $checkboxCtrl, $rootCtrl;

    this.SetAttribute = function (data) {
        ///UserCodeRegionStart:[SetAttribute] (do not remove this comment.)		
        this.SetValue(data);
        ///UserCodeRegionEnd: (do not remove this comment.)
    }

    // Databinding for property Attribute
    this.GetAttribute = function () {
        ///UserCodeRegionStart:[GetAttribute] (do not remove this comment.)		
        return this.Value;
        ///UserCodeRegionEnd: (do not remove this comment.)
    }

    this.SetValue = function (newValue) {
        if (typeof (newValue) === 'boolean')
            this.Checked = newValue == gx.lang.booleanValue(this.CheckedValue);
        else {
            this.Checked = this.CheckedValue == newValue;
        }
        this.Value = newValue;
		
		if ($checkboxCtrl) { //already initialized
			$checkboxCtrl.bootstrapSwitch('state', this.Checked, false);
		}
    }

    this.SetState = function (newState) {
        if (newState)
            this.Value = this.CheckedValue;
        else
            this.Value = this.UncheckedValue;
        this.Checked = newState;
    }

    this.show = function () {        
        if (!this.IsPostBack) {            
            setTimeout(this.init.closure(this), 0);
        }
        else {            
			$checkboxCtrl.bootstrapSwitch('readonly', !this.Enabled);
			$checkboxCtrl.bootstrapSwitch('state', this.Checked, false);
			$rootCtrl.toggle(this.Visible);
        }        
        
    }
    this.init = function () {
        $container = $(this.getContainerControl());
        baseId = this.ContainerName.replace(this.DesignContainerName, this.DesignContainerName.replace(/Container$/, "")) + '_checkbox';
        var html = Mustache.to_html("<input type='checkbox' id='{{id}}'/>", { id: baseId });
        this.setHtml(html);
        var className = 'wrapper gx-switch-' + this.Class;
        var opts = { 'size': 'small', 'onColor': 'success on', 'offColor': 'default off', 'wrapperClass': className, 'readonly': !this.Enabled, 'state': this.Checked, onText:this.OnText , offText:this.OffText};
        $checkboxCtrl = $('#' + baseId).bootstrapSwitch(opts);
        var self = this;

        $checkboxCtrl.on('switchChange.bootstrapSwitch', function (event, state) {
            self.SetState(state);
            if (self.ControlValueChanged)
                self.ControlValueChanged();
        });
        $rootCtrl = $checkboxCtrl.closest('.form-group');        
        $rootCtrl = !$rootCtrl.length? $container: $rootCtrl;		
        $rootCtrl.toggle(this.Visible);
		$rootCtrl.find('label').attr('for', baseId);
    }


}