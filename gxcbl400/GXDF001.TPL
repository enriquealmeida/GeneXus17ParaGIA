:Procedure
  :Info
    Name=#1
    Description=Cobol date fix - #2
    Folder=#3
  :eInfo
  :Variable\
    Name=#4\
    Title=#5\
    BasedOn=#5\
  :eVariable
  :Variable
    Name=TblName
    Title=TblName
    Type=Character
    Length=30
    Decimals=0
  :eVariable
  :Variable
    Name=RcdCount
    Title=RcdCount
    Length=9
    Decimals=0
    Picture=ZZZZZZZZ9
  :eVariable
  :Variable
    Name=RcdChg
    Title=RcdChg
    Length=9
    Decimals=0
    Picture=ZZZZZZZZ9
  :eVariable
  :Variable
    Name=RcdKeyChg
    Title=RcdKeyChg
    Length=9
    Decimals=0
    Picture=ZZZZZZZZ9
  :eVariable
  :Variable
    Name=RcdChgFlg
    Title=RcdChgFlg
    Length=1
    Decimals=0
    Picture=9
  :eVariable
  :Property
    Type=string
    Name=CONFIRM
    Value=NO
    Rule=noconfirm();
  :eProperty
  :Property
    Type=string
    Name=TRNCMT
    Value=NO
    Rule=nocommitment();
  :eProperty
  :Property
    Type=string
    Name=OUTPUT
    Value=PRN
    Rule=Output('PRN');
  :eProperty
  :Layout
    :FormInfo
      FormId=1
      FormClass=Text
      IsGraphic=N
      IsDefault=N
      HasGrid
      TextGrid
      :Att:Font
        Name=Courier New
        Size=9
      :eFont
      :Txt:Font
        Bold
      :eFont
    :eFormInfo
    :Block
      :CodeBlock
        left=17
        top=1
        bottom=76
        Brush
        LBColor=Silver
        TextMode
        :ColorInfo
          Foreground=Lime
        :eColorInfo
        :WebProperties
        :eWebProperties
        FromStyle=N
        FromDefault=N
        Shape=Frame
        OutStyle=Single
        :Source
