       PGM PARM(&PGMLIB &DTALIB &APPNAME &GXLIB)

       DCL VAR(&PGMLIB)     TYPE(*CHAR)     LEN(10)
       DCL VAR(&DTALIB)     TYPE(*CHAR)     LEN(10)
       DCL VAR(&APPNAME)    TYPE(*CHAR)     LEN(30)
       DCL VAR(&GXLIB)      TYPE(*CHAR)     LEN(10)

/* Standard error-handling variables                                 */
       DCL       VAR(&ERRORSW)       TYPE(*LGL)
       DCL       VAR(&MSGID)         TYPE(*CHAR)         LEN(7)
       DCL       VAR(&MSGDTA)        TYPE(*CHAR)         LEN(79)
       DCL       VAR(&MSGF)          TYPE(*CHAR)         LEN(10)
       DCL       VAR(&MSGFLIB)       TYPE(*CHAR)         LEN(10)

       MONMSG    MSGID(CPF0000 GXM0000) +
                 EXEC(GOTO CMDLBL(STDERR1))

MAIN:
       /* Create PROGRAMS LIBRARY */
       CRTLIB    LIB(&PGMLIB) +
                 TEXT(&APPNAME) +
                 TYPE(*PROD)
       MONMSG    CPF2111

       /* Check for data base existence*/
       CHKOBJ    OBJ(QSYS/&PGMLIB) +
                 OBJTYPE(*LIB)

       /* Change Description */
       CHGOBJD   OBJ(&PGMLIB) +
                 OBJTYPE(*LIB) +
                 TEXT(&APPNAME)

       /* Create CL Source File        */
       CRTSRCPF FILE(&PGMLIB/QCLSRC)     +
                TEXT('CL Source File')  +
                AUT(*USE)
       MONMSG    CPF7302

       /* Create COBOL Source File     */
       CRTSRCPF FILE(&PGMLIB/QLBLSRC)    +
                TEXT('COBOL Source File')       +
                AUT(*USE)
       MONMSG    CPF7302

       /* Create RPG Source File     */
       CRTSRCPF FILE(&PGMLIB/QRPGSRC)    +
                TEXT('RPG Source File')       +
                AUT(*USE)
       MONMSG    CPF7302

       /* Create DDS Source File       */
       CRTSRCPF FILE(&PGMLIB/QDDSSRC)    +
                TEXT('DDS Source File') +
                AUT(*USE)
       MONMSG    CPF7302

       /* Create Programs List Source File */
       CRTSRCPF FILE(&PGMLIB/GXAPPPGM)    +
                TEXT('List of programs in application') +
                AUT(*USE)
       MONMSG    CPF7302

       /* Add an empty member to avoid errors */
       ADDPFM   FILE(&PGMLIB/GXAPPPGM)    +
                MBR(GXAPPPGM) +
                TEXT('Initially empty member')
       MONMSG    CPF7306

       /* Create File List Source File */
       CRTSRCPF FILE(&PGMLIB/GXFLST)    +
                TEXT('Sources for file creation') +
                AUT(*USE)
       MONMSG    CPF7302

       /* Create DATA LIBRARY */
       CRTLIB    LIB(&DTALIB) +
                 TEXT(&APPNAME) +
                 TYPE(*PROD)
       MONMSG    CPF2111

       /* Check for data base existence*/
       CHKOBJ    OBJ(QSYS/&DTALIB) +
                 OBJTYPE(*LIB)

       /* Change Description */
       CHGOBJD   OBJ(&DTALIB) +
                 OBJTYPE(*LIB) +
                 TEXT(&APPNAME)

       CRTJRNRCV JRNRCV(&DTALIB/QSQJRN0001)
       MONMSG    CPF7010

       CRTJRN JRN(&DTALIB/QSQJRN) JRNRCV(&DTALIB/QSQJRN0001)
       MONMSG    CPF7015


PGMEND:
       RETURN
/* Standard error-handling routine */
STDERR1:

       IF        COND(&ERRORSW) +
                 THEN(SNDPGMMSG MSGID(CPF9999) +
                           MSGF(QCPFMSG) +
                           MSGTYPE(*ESCAPE))

       CHGVAR    VAR(&ERRORSW) +
                 VALUE('1')
STDERR2:
       RCVMSG    MSGTYPE(*DIAG) +
                 MSGDTA(&MSGDTA) +
                 MSGID(&MSGID) +
                 MSGF(&MSGF) +
                 MSGFLIB(&MSGFLIB)

       IF        COND(&MSGID *EQ '       ') +
                 THEN(GOTO CMDLBL(STDERR3))

       SNDPGMMSG MSGID(&MSGID) +
                 MSGF(&MSGFLIB/&MSGF) +
                 MSGDTA(&MSGDTA) +
                 MSGTYPE(*DIAG)

       GOTO      CMDLBL(STDERR2)
STDERR3:
       RCVMSG    MSGTYPE(*EXCP) +
                 MSGDTA(&MSGDTA) +
                 MSGID(&MSGID) +
                 MSGF(&MSGF) +
                 MSGFLIB(&MSGFLIB)

       SNDPGMMSG MSGID(&MSGID) +
                 MSGF(&MSGFLIB/&MSGF) +
                 MSGDTA(&MSGDTA) +
                 MSGTYPE(*ESCAPE)

/* End of program */
       ENDPGM
