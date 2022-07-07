/*-------------------------------------------------------------------*/
/* GeneXus Cobol/400 Generator.                                      */
/*                                                                   */
/*     Create a Cobol Program.                                       */
/*                                                                   */
/*                                                                   */
/*-------------------------------------------------------------------*/
       PGM       
/* Internal variables                                                */
       DCL VAR(&PGMNAME) TYPE(*CHAR) LEN(10) VALUE(#1)
       DCL VAR(&DTALIB)  TYPE(*CHAR) LEN(10) VALUE(#4) 
       DCL VAR(&PGMLIB)  TYPE(*CHAR) LEN(10) VALUE(#5)
       DCL VAR(&OBJTYPE) TYPE(*CHAR) LEN(15) VALUE(#6)
       DCL VAR(&RMVOBS)  TYPE(*CHAR) LEN(01) VALUE('#13')
       DCL VAR(&DLTSRC)  TYPE(*CHAR) LEN(01) VALUE('#11')
       DCL VAR(&DLTSPLF) TYPE(*CHAR) LEN(01) VALUE('#23')
       DCL VAR(&MSGTYPE) TYPE(*CHAR) LEN(07)
       DCL VAR(&OBJDESC) TYPE(*CHAR) LEN(50)
/* Standard error-handling variables                                 */
       DCL       VAR(&ERRORSW)       TYPE(*LGL)
       DCL       VAR(&MSGID)         TYPE(*CHAR)         LEN(7)
       DCL       VAR(&MSGDTA)        TYPE(*CHAR)         LEN(79)
       DCL       VAR(&MSGF)          TYPE(*CHAR)         LEN(10)
       DCL       VAR(&MSGFLIB)       TYPE(*CHAR)         LEN(10)

       MONMSG    MSGID(CPF0000 GXM0000) +
                 EXEC(GOTO CMDLBL(STDERR1))

/* Create Display File */                                          \
       CRTDSPF   FILE(&PGMLIB/#15) SRCFILE(&PGMLIB/QDDSSRC) +      \
                 OPTION(*NOSRC *NOLIST) AUT(*USE) RSTDSP(*NO) +    \
                 TEXT('#17 : Scren file')
       MONMSG    MSGID(CPF7211 CPF7302) EXEC(DO)                   \
                 CHGVAR    VAR(&MSGDTA) VALUE(#15 *CAT +           \
                           &PGMNAME *CAT &PGMLIB *CAT &OBJTYPE)    \
                 CHGVAR    VAR(&MSGID) VALUE(GXM0102)              \
                 CHGVAR    VAR(&MSGTYPE) VALUE(*ESCAPE)            \
                 GOTO      CMDLBL(PGMEND)                          \
                 ENDDO

/* Create Printer File */                                          \
       CRTPRTF   FILE(&PGMLIB/#19) SRCFILE(&PGMLIB/QDDSSRC) +      \
                 OPTION(*NOSRC *NOLIST) AUT(*USE) DEVTYPE(*SCS) +  \
                 TEXT('#17 : Printer file')
       MONMSG    MSGID(CPF7211 CPF7302) EXEC(DO)                   \
                 CHGVAR    VAR(&MSGDTA) VALUE( #19 *CAT +          \
                           &PGMNAME *CAT &PGMLIB *CAT &OBJTYPE)    \
                 CHGVAR    VAR(&MSGID) VALUE(GXM0102)              \
                 CHGVAR    VAR(&MSGTYPE) VALUE(*ESCAPE)            \
                 GOTO      CMDLBL(PGMEND)                          \
                 ENDDO                                             

/* Setup library list and File Overrides if required */
       RMVLIBLE  LIB(#12)            \
       MONMSG    MSGID(CPF2104)      \
       ADDLIBLE  LIB(#12)            \
       MONMSG    MSGID(CPF2103)      
       OVRDBF    FILE(#7)  TOFILE(#8)
       OVRDSPF   FILE(#14) TOFILE(&PGMLIB/#15)

/* Create Cobol Program */
       CRTCBLPGM PGM(&PGMLIB/&PGMNAME) +
                 SRCFILE(&PGMLIB/QLBLSRC) +
                 OPTION(*APOST +
                        #9  +
                       ) +
                 GENOPT( +
                        #10  +
                       ) +
                 CVTOPT( +
                        #22 +
                       ) +
                 TEXT('#17') +
                 TGTRLS(#20) +
                 AUT(*USE)
       MONMSG   MSGID(LBL9001) EXEC(DO)
                CHGVAR    VAR(&MSGDTA) VALUE(&PGMNAME *CAT +
                                             &PGMLIB *CAT +
                                             &OBJTYPE)
                CHGVAR    VAR(&MSGID) VALUE(GXM0103)
                CHGVAR    VAR(&MSGTYPE) VALUE(*ESCAPE)
                GOTO      CMDLBL(PGMEND)
                ENDDO
       IF COND(&DLTSPLF *EQ 'Y') THEN(DO)
/* Delete spool files                                                */
          DLTSPLF   FILE(&PGMNAME)           SPLNBR(*LAST)
          MONMSG    CPF0000
       ENDDO

/* Cl programs required for SUBMIT rule/command */
CRTCLPGM  PGM(&PGMLIB/#18) +\
          SRCFILE(&PGMLIB/QCLSRC) +\
          TGTRLS(#20) +\
          AUT(*USE) \
MONMSG    MSGID(CPF0801) EXEC(DO) \
  CHGVAR VAR(&MSGDTA) VALUE('Submit program #18 NOT created.') \
  CHGVAR VAR(&MSGID) VALUE(GXM9999) \
  CHGVAR VAR(&MSGTYPE) VALUE(*ESCAPE) \
  GOTO   CMDLBL(PGMEND) \
ENDDO
/* End of Cl programs */

/* Create Stub program */
CRTCBLPGM PGM(&PGMLIB/#21) SRCFILE(&PGMLIB/QLBLSRC) + \
          OPTION(*APOST + \
                 #9 + \
                ) + \
          GENOPT( + \
                 #10 + \
                ) +\
          TEXT('Stub program for #1') +\
          TGTRLS(#20) +\
          AUT(*USE) \
MONMSG    MSGID(LBL9001) EXEC(DO) \
  CHGVAR VAR(&MSGDTA) VALUE('Stub program for #1 NOT created.') \
  CHGVAR VAR(&MSGID) VALUE(GXM9999) \
  CHGVAR VAR(&MSGTYPE) VALUE(*ESCAPE) \
  GOTO   CMDLBL(PGMEND) \
ENDDO
IF COND(&DLTSPLF *EQ 'Y') THEN(DO)
   DLTSPLF   FILE(#21)                SPLNBR(*LAST) \
   MONMSG    CPF0000
ENDDO

/* End Stub program */

       CHGVAR    VAR(&MSGDTA) VALUE(&PGMNAME *CAT &PGMLIB *CAT &OBJTYPE)
       CHGVAR    VAR(&MSGID) VALUE(GXM0101)
       CHGVAR    VAR(&MSGTYPE) VALUE(*COMP)

PGMEND:
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
         CHGPGM  PGM(&PGMLIB/#18) RMVOBS(*ALL) 
       ENDDO
/* Source deletion                                                   */
       IF COND(&DLTSRC *EQ 'Y') THEN(DO)
         RMVM      FILE(&PGMLIB/QLBLSRC)    MBR(&PGMNAME)
         MONMSG    MSGID(CPF7310)
         RMVM      FILE(&PGMLIB/QLBLSRC)    MBR(#21) \
         MONMSG    MSGID(CPF7310)
         RMVM      FILE(&PGMLIB/QCLSRC)     MBR(#3)
         MONMSG    MSGID(CPF7310)
         RMVM      FILE(&PGMLIB/QDDSSRC)    MBR(#15)               \
         MONMSG    MSGID(CPF7310)
         RMVM      FILE(&PGMLIB/QCLSRC)     MBR(#18)               \
         MONMSG    MSGID(CPF7310)
         RMVM      FILE(&PGMLIB/QDDSSRC)    MBR(#19)               \
         MONMSG    MSGID(CPF7310)
       ENDDO
       ENDPGM
