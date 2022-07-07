/*-------------------------------------------------------------------*/
/* GeneXus Menu/400  Generator.                                      */
/*                                                                   */
/*     Create a menu program                                         */
/*                                                                   */
/*                                                                   */
/*-------------------------------------------------------------------*/
       PGM       
/* Internal variables                                                */
       DCL VAR(&PGMNAME) TYPE(*CHAR) LEN(10) VALUE(#1)
       DCL VAR(&SCRNAME) TYPE(*CHAR) LEN(10) VALUE(#2)
       DCL VAR(&DTALIB)  TYPE(*CHAR) LEN(10) VALUE(#4)
       DCL VAR(&PGMLIB)  TYPE(*CHAR) LEN(10) VALUE(#5)
       DCL VAR(&OBJTYPE) TYPE(*CHAR) LEN(15) VALUE(#6)
       DCL VAR(&RMVOBS)  TYPE(*CHAR) LEN(01) VALUE('#11')
       DCL VAR(&DLTSRC)  TYPE(*CHAR) LEN(01) VALUE('#7')
       DCL VAR(&MSGTYPE) TYPE(*CHAR) LEN(07)
       DCL VAR(&OBJDESC) TYPE(*CHAR) LEN(50)
/* Standard error-handling variables                                 */
       DCL VAR(&ERRORSW) TYPE(*LGL)
       DCL VAR(&MSGID)   TYPE(*CHAR) LEN(7)
       DCL VAR(&MSGDTA)  TYPE(*CHAR) LEN(79)
       DCL VAR(&MSGF)    TYPE(*CHAR) LEN(10)
       DCL VAR(&MSGFLIB) TYPE(*CHAR) LEN(10)

       MONMSG    MSGID(CPF0000 GXM0000) +
                 EXEC(GOTO CMDLBL(STDERR1))
/* Create display file */                                          #8
       OVRDSPF   FILE(&SCRNAME) TOFILE(&PGMLIB/&SCRNAME)           #8
       CRTDSPF   FILE(&PGMLIB/&SCRNAME) SRCFILE(&PGMLIB/QDDSSRC) + #8
                 OPTION(*NOSRC *NOLIST) AUT(*USE) RSTDSP(*NO) +    #8
                 TEXT('#10 : Screen file')                         #8
       MONMSG    MSGID(CPF7211 CPF7302) EXEC(DO)                   #8
                 CHGVAR    VAR(&MSGDTA) VALUE(&SCRNAME *CAT +      #8
                           &PGMNAME *CAT &PGMLIB *CAT &OBJTYPE)    #8
                 CHGVAR    VAR(&MSGID) VALUE(GXM0102)              #8
                 CHGVAR    VAR(&MSGTYPE) VALUE(*ESCAPE)            #8
                 GOTO      CMDLBL(PGMEND)                          #8
                 ENDDO                                             #8
CRTOBJ:
       /* Create program */
       CRTCLPGM  PGM(&PGMLIB/&PGMNAME) SRCFILE(&PGMLIB/QCLSRC) +
                 TGTRLS(#12) AUT(*USE)
       MONMSG    MSGID(CPF0801) EXEC(DO)
                 CHGVAR VAR(&MSGDTA) VALUE(&PGMNAME *CAT &PGMLIB *CAT +
                                           &OBJTYPE)
                 CHGVAR VAR(&MSGID) VALUE(GXM0103)
                 CHGVAR VAR(&MSGTYPE) VALUE(*ESCAPE)
                 GOTO   CMDLBL(PGMEND)
       ENDDO
       CHGOBJD   OBJ(&PGMLIB/&PGMNAME) OBJTYPE(*PGM) +
                 TEXT('#10')
       CHGVAR    VAR(&MSGDTA) VALUE(&PGMNAME *CAT &PGMLIB *CAT &OBJTYPE)
       CHGVAR    VAR(&MSGID) VALUE(GXM0101)
       CHGVAR    VAR(&MSGTYPE) VALUE(*COMP)
PGMEND:
/* Delete spool files                                                */
       DLTSPLF   FILE(&PGMNAME)      SPLNBR(*LAST)
       MONMSG    CPF0000

/* Normal program end                                                */
       CALL      PGM(GXUSRMSG) PARM(&MSGID &MSGDTA &MSGTYPE)
       GOTO      CMDLBL(ENDOFPGM)

/* Standard error-handling routine                                   */
STDERR1:
       IF COND(&ERRORSW) THEN(SNDPGMMSG MSGID(CPF9999) MSGF(QCPFMSG) +
                                        MSGTYPE(*ESCAPE))
       CHGVAR    VAR(&ERRORSW) VALUE('1')
STDERR2:
       RCVMSG MSGTYPE(*DIAG) MSGDTA(&MSGDTA) MSGID(&MSGID) MSGF(&MSGF) +
              MSGFLIB(&MSGFLIB)
       IF COND(&MSGID *EQ '       ') THEN(GOTO CMDLBL(STDERR3))
       SNDPGMMSG MSGID(&MSGID) MSGF(&MSGFLIB/&MSGF) MSGDTA(&MSGDTA) +
                 MSGTYPE(*DIAG)
       GOTO CMDLBL(STDERR2)
STDERR3:
       RCVMSG MSGTYPE(*EXCP) MSGDTA(&MSGDTA) MSGID(&MSGID) MSGF(&MSGF) +
              MSGFLIB(&MSGFLIB)
       SNDPGMMSG MSGID(&MSGID) MSGF(&MSGFLIB/&MSGF) MSGDTA(&MSGDTA) +
                 MSGTYPE(*ESCAPE)
ENDOFPGM:
/* End of program                                                    */
       DLTOVR    FILE(*ALL)
/* Removing observability of programs                                */
       IF COND(&RMVOBS *EQ 'Y') THEN(DO)
         CHGPGM  PGM(&PGMLIB/&PGMNAME) RMVOBS(*ALL)
       ENDDO
/* Source deletion                                                   */
       IF COND(&DLTSRC *EQ 'Y') THEN(DO)
         RMVM      FILE(&PGMLIB/QCLSRC)    MBR(&PGMNAME)
         MONMSG    MSGID(CPF7310)
         RMVM      FILE(&PGMLIB/QCLSRC)    MBR(#3)
         MONMSG    MSGID(CPF7310)
         RMVM      FILE(&PGMLIB/QDDSSRC)   MBR(&SCRNAME)  #9
         MONMSG    MSGID(CPF7310)                         #9
       ENDDO
       ENDPGM
