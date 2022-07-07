
        MONMSG MSGID(CPF0000) EXEC(GOTO MAINLOOP)

        #5

     /*   RMVLIBLE #2   */
     /*   MONMSG MSGID(CPF2104)   */
     /*   ADDLIBLE #2   */

     /*   RMVLIBLE #4   */
     /*   MONMSG MSGID(CPF2104)   */
     /*   ADDLIBLE #4   */

     /*   RMVLIBLE #3   */
     /*   MONMSG MSGID(CPF2104)   */
     /*   ADDLIBLE #3   */

        RMVMSG CLEAR(*ALL)

        CHGVAR  &I              0
        CHGVAR  &PGMNAM         '#1'
        CHGVAR  &GXLIB          '#2'
        CHGVAR  &DTALIB         '#3'
        CHGVAR  &PGMLIB         '#4'
        CHGVAR  &CKLIN          1
