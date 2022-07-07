/*-------------------------------------------------------------------*/
/* GeneXus RPG/400 Generator.                                        */
/*                                                                   */
/*     Create a RPG/400 Program.                                     */
/*                                                                   */
/*                                                                   */
/*-------------------------------------------------------------------*/
       PGM
/* Internal variables                                                */
       DCL      VAR(&MSGTYPE) TYPE(*CHAR) LEN(7)
       DCL      VAR(&PGMLIB)  TYPE(*CHAR) LEN(10) VALUE(#3)
       DCL      VAR(&PGMNAME) TYPE(*CHAR) LEN(10) VALUE(#4)
/* Standard error-handling variables                                 */
       DCL      VAR(&ERRORSW) TYPE(*LGL)
       DCL      VAR(&MSGID)   TYPE(*CHAR) LEN(7)
       DCL      VAR(&MSGDTA)  TYPE(*CHAR) LEN(79)
       DCL      VAR(&MSGF)    TYPE(*CHAR) LEN(10)
       DCL      VAR(&MSGFLIB) TYPE(*CHAR) LEN(10)
/* Standard error-handling monitoring                                */
       MONMSG   MSGID(CPF0000 GXM0000) +
                EXEC(GOTO CMDLBL(STDERR1))
/* Main program flow                                                 */
       RMVLIBLE LIB(#12)  \
       MONMSG   MSGID(CPF0000) \
       ADDLIBLE LIB(#12)

       RMVM     FILE(#3/QCLSRC) MBR(#7)                   #8
       MONMSG   MSGID(CPF5715 CPF5815 CPF7310)            #8
/* Overwrite DataBase Files */                                          
       OVRDBF   FILE(#1) TOFILE(#2)
       
/*       RMVM     FILE(#3/QRPGLESRC) MBR(#4)		   */
/*       MONMSG MSGID(CPF5715 CPF5815 CPF7310)		   */

/*       CRTSRCPF  FILE(#3/QRPGLESRC) RCDLEN(112)	   */
/*       MONMSG MSGID(CPF0000)						   */


       /* CVTRPGSRC  FROMFILE(#3/QRPGSRC) FROMMBR(#4) + */
       /*           TOFILE(#3/QRPGLESRC) TOMBR(#4) +    */
       /*           LOGFILE(*NONE) CVTRPT(*NO)          */

     /*  MONMSG     MSGID(RNF0000) EXEC(DO) */

 /*        RMVM   FILE(#3/QRPGLESRC) MBR(#4)               #8 */
 /*        MONMSG MSGID(CPF5715 CPF5815 CPF7310)           #8	  */
 /*															  */
 /*        CHGVAR     VAR(&MSGDTA) +							  */
 /*                   VALUE(&PGMNAME *CAT +					  */
 /*                         &PGMLIB *CAT +					  */
 /*                         '#11')							  */
 /*															  */
 /*        CHGVAR     VAR(&MSGID) +							  */
 /*                   VALUE(GXM0103)							  */
 /*															  */
 /*        CHGVAR     VAR(&MSGTYPE) +							  */
 /*                   VALUE(*ESCAPE)							  */
 /*															  */
 /*        GOTO       CMDLBL(PGMEND)							  */
 /*															  */
 /*      ENDDO												  */

       CRTRPGMOD  MODULE(#3/#4) +
                  SRCFILE(#3/QRPGLESRC) +
                  GENLVL(15) +
                  FIXNBR(*ZONED) +                      #9
                  INDENT('|') +
                  OPTION(     +
                          #5  +
                            ) +
                  OUTPUT(*PRINT) +                      #6
                  OPTIMIZE(*NONE) +                     #16
                  AUT(*USE) TGTRLS(#17) +
                  CVTOPT(*DATETIME *VARCHAR *GRAPHIC)

       MONMSG     MSGID(RNS9309) EXEC(DO)

         RMVM   FILE(#3/QRPGSRC) MBR(#4)                 #8
         MONMSG MSGID(CPF5715 CPF5815 CPF7310)           #8

         RMVM   FILE(#3/QRPGLESRC) MBR(#4)               #8
         MONMSG MSGID(CPF5715 CPF5815 CPF7310)           #8

         CHGVAR     VAR(&MSGDTA) +
                    VALUE(&PGMNAME *CAT +
                          &PGMLIB *CAT +
                          '#11')

         CHGVAR     VAR(&MSGID) +
                    VALUE(GXM0103)

         CHGVAR     VAR(&MSGTYPE) +
                    VALUE(*ESCAPE)

         GOTO       CMDLBL(PGMEND)

       ENDDO

       RMVM   FILE(#3/QRPGSRC) MBR(#4)                   #8
       MONMSG MSGID(CPF5715 CPF5815 CPF7310)             #8

       RMVM   FILE(#3/QRPGLESRC) MBR(#4)               #8
       MONMSG MSGID(CPF5715 CPF5815 CPF7310)           #8

       CRTPGM (#3/#4) MODULE(#3/#4 GXCGIFRM) +
       TGTRLS(#17)
       MONMSG     MSGID(CPF0000) EXEC(DO)

         CHGVAR     VAR(&MSGDTA) +
                    VALUE(&PGMNAME *CAT +
                          &PGMLIB *CAT +
                          '#11')

         CHGVAR     VAR(&MSGID) +
                    VALUE(GXM0103)

         CHGVAR     VAR(&MSGTYPE) +
                    VALUE(*ESCAPE)

         GOTO       CMDLBL(PGMEND)

       ENDDO

       CHGPGM PGM(#3/#4) RMVOBS(*ALL)                     #10
       CHGOBJD   OBJ(&PGMLIB/&PGMNAME) OBJTYPE(*PGM) +
                 TEXT('#14')

/* Cl programs required for SUBMIT rule/command */
CRTCLPGM  PGM(&PGMLIB/#15) + \
SRCFILE(&PGMLIB/QCLSRC) AUT(*USE) + \
TGTRLS(#17) \
MONMSG    MSGID(CPF0801) EXEC(DO) \
  CHGVAR VAR(&MSGDTA) VALUE('Submit program #15 not created') \
  CHGVAR VAR(&MSGID) VALUE(GXM9999) \
  CHGVAR VAR(&MSGTYPE) VALUE(*ESCAPE) \
  GOTO   CMDLBL(PGMEND) \
ENDDO
/* End of Cl programs */


/* Delete spool files                                                */
       CHGVAR     VAR(&MSGDTA) +
                  VALUE(&PGMNAME *CAT +
                        &PGMLIB *CAT +
                        '#11')

       CHGVAR     VAR(&MSGID) +
                  VALUE(GXM0101)

       CHGVAR     VAR(&MSGTYPE) +
                  VALUE(*COMP)

 PGMEND:
       CALL       PGM(GXUSRMSG) +
                  PARM(&MSGID &MSGDTA &MSGTYPE)

/* Normal program end                                                */
             GOTO CMDLBL(ENDOFPGM)

/* Standard error-handling routine                                   */
 STDERR1:
             IF         COND(&ERRORSW) +
                        THEN(SNDPGMMSG MSGID(CPF9999) +
                                       MSGF(QCPFMSG) +
                                       MSGTYPE(*ESCAPE))

             CHGVAR     VAR(&ERRORSW) +
                        VALUE('1')
 STDERR2:
             RCVMSG     MSGTYPE(*DIAG) +
                        MSGDTA(&MSGDTA) +
                        MSGID(&MSGID) +
                        MSGF(&MSGF) +
                        MSGFLIB(&MSGFLIB)

             IF         COND(&MSGID *EQ '       ') +
                        THEN(GOTO CMDLBL(STDERR3))

             SNDPGMMSG  MSGID(&MSGID) +
                        MSGF(&MSGFLIB/&MSGF) +
                        MSGDTA(&MSGDTA) +
                        MSGTYPE(*DIAG)

             GOTO       CMDLBL(STDERR2)
 STDERR3:
             RCVMSG     MSGTYPE(*EXCP) +
                        MSGDTA(&MSGDTA) +
                        MSGID(&MSGID) +
                        MSGF(&MSGF) +
                        MSGFLIB(&MSGFLIB)

             SNDPGMMSG  MSGID(&MSGID) +
                        MSGF(&MSGFLIB/&MSGF) +
                        MSGDTA(&MSGDTA) +
                        MSGTYPE(*ESCAPE)
/*-------------------------------------------------------------------*/
/* End of program                                                    */
/*-------------------------------------------------------------------*/
ENDOFPGM:
             DLTOVR FILE(*ALL)
             ENDPGM
