; test-strcmp.s - tests for the strcmp routine

                .data
; Area for storing parameters for strcmp
strcmp_params:  .space 8
                .space 8
; Test strings
edu1:           .asciiz "EduMIPS64"
edu2:           .asciiz "EduMIPS64"
empty:          .space 8
test:           .asciiz "Test"

; Data for the error reporting routine
test_failed:    .asciiz "Test failed"
error_params:   .byte       2       ; stderr
error_addr:     .word64     0
                .byte       11

                .code


daddi R1, R0, 0
daddi R2, R0, 20
loop: daddi R1, R1, 5
	  bne R1, R2, loop

END: daddi R3, R2, 5
