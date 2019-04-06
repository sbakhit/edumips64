.data

.code
daddi r2,r0,15          ; store 15 in r2
daddi r4,r0,2          ; store 0 in r3
daddi r3,r0,0          ; store 0 in r3 (d)
start:          bne r3,r4,increment
                daddi r3,r0,0
                b loopincrement
increment:      daddi r3,r3,1
loopincrement:  daddi r1, r1, 1 ; increment r1
                bne	r1,r2,start	 ; loop back if r1 != 15

quit:		SYSCALL 0		; exit