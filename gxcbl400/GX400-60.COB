SET MODIFIED-MARK-OFF TO TRUE
MOVE ZEROES TO GX-SFL-PAGE-COUNT
PERFORM
  UNTIL #4 = 1 OR
        GX-SFL-PAGE-COUNT >= #3
  PERFORM #1
  PERFORM #2
END-PERFORM
MOVE #4 TO GX-LREC
