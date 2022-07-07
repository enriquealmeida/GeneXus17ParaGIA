GX-FILE-STATUS-ANALYSIS.

    EVALUATE TRUE
       WHEN V-FS = '00' OR
            V-FS = '41' OR
            V-FS = '42' OR
            V-FS = '43' OR
            V-FS = '94' OR
            V-FS = '95'
          SET GX-IO-OK           TO TRUE

       WHEN V-FS = '10' OR
            V-FS = '23' OR
            V-FS = '46'
          SET GX-IO-EOF          TO TRUE

       WHEN V-FS = '92' OR
            V-FS = '90'
          SET GX-IO-LOCKED-FILE  TO TRUE

       WHEN V-FS = '9D'
          SET GX-IO-LOCKED-RCD   TO TRUE

       WHEN V-FS = '22'
          SET GX-IO-DUP-KEY      TO TRUE

       WHEN V-FS = '43' OR
            V-FS = '94' OR
            V-FS = '9S'
          SET GX-IO-MISSING-READ TO TRUE

       WHEN V-FS = '04'
          SET GX-IO-BAD-RCD-LEN  TO TRUE

       WHEN OTHER
          SET GX-IO-OTHER-ERRORS TO TRUE
    END-EVALUATE
    CONTINUE.
