.data

.code
daddi r2,r0,15          ; store 15 in r2
start:  daddi r1, r1, 1 ; increment r1
		bne	r1,r2,start	 ; loop back if r1 != 15

quit:		SYSCALL 0		; exit