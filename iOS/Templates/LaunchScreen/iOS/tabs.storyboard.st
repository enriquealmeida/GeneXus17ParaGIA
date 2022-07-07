    <scenes>
        <!--View Controller-->
        <scene sceneID="elT-GD-Q4O">
            <objects>
                <viewController id="vNx-eZ-cBs" sceneMemberID="viewController">
                    <view key="view" contentMode="scaleToFill" id="Efe-yl-ctR">
                        <autoresizingMask key="autoresizingMask" widthSizable="YES" heightSizable="YES"/>
                        <color key="backgroundColor" systemColor="systemBackgroundColor" cocoaTouchSystemColor="whiteColor"/>
                        <viewLayoutGuide key="safeArea" id="HWp-Nj-Taq"/>
                    </view>
                    <tabBarItem key="tabBarItem" title="" id="Ekv-2J-PLS"/>
                    <navigationItem key="navigationItem" id="wdq-eY-92R"/>
                </viewController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="yUy-zJ-o2V" userLabel="First Responder" customClass="UIResponder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="-372" y="-150"/>
        </scene>
        <!--Navigation Controller-->
        <scene sceneID="8OE-iu-6B3">
            <objects>
                <navigationController id="cqO-58-rEn" sceneMemberID="viewController">
                    <tabBarItem key="tabBarItem" title="" id="1lG-mp-d82"/>
                    <navigationBar key="navigationBar" contentMode="scaleToFill" insetsLayoutMarginsFromSafeArea="NO" id="MEI-b5-eeW">
                        <autoresizingMask key="autoresizingMask"/>
                    </navigationBar>
                    <connections>
                        <segue destination="vNx-eZ-cBs" kind="relationship" relationship="rootViewController" id="dsR-ZH-4yS"/>
                    </connections>
                </navigationController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="kHC-x3-kfh" userLabel="First Responder" customClass="UIResponder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="-1071" y="-150"/>
        </scene>
        <!--Tab Bar Controller-->
        <scene sceneID="Rz5-cL-25w">
            <objects>
                <tabBarController id="$LaunchScreen.InitialViewControllerId$" sceneMemberID="viewController">
                    <tabBar key="tabBar"$if(LaunchScreen.TabBarHidden)$ hidden="YES"$endif$ contentMode="scaleToFill" insetsLayoutMarginsFromSafeArea="NO" id="7TO-gU-cDa">
                        <autoresizingMask key="autoresizingMask"/>
                        <color key="tintColor" white="0.0" alpha="0.0" colorSpace="custom" customColorSpace="genericGamma22GrayColorSpace"/>
                        <color key="selectedImageTintColor" white="0.0" alpha="0.0" colorSpace="custom" customColorSpace="genericGamma22GrayColorSpace"/>
$if(LaunchScreen.TabBarHiddenVariationOniPad)$
                        <variation key="heightClass=regular-widthClass=regular" hidden="$if(LaunchScreen.TabBarHidden)$NO$else$YES$endif$"/>
$endif$
                    </tabBar>
                    <connections>
                        <segue destination="cqO-58-rEn" kind="relationship" relationship="viewControllers" id="jMA-Mg-VlB"/>
                    </connections>
                </tabBarController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="7s8-ml-AEb" userLabel="First Responder" customClass="UIResponder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="-1781" y="-150"/>
        </scene>
    </scenes>