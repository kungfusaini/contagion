Contagion!

Code availble to edit, seperate GUI added.

 * Improvements:
 *       -Update Legend
 *
 *      -Add a probability of infection so in the future it can be changed to different values. Note:
 *       when two people meet, the method meet gets called multiple times, this could affect the probability.
 *       When a person A meets person B, both can get infected, when person B meets person A in its object, both
 *       will get infected again, this will increase the probability and has to be changed.
 *
 *       -When someone is infected it will stop his movement, but just from day 5 of infection.
 *
 *       -Some people don't get the symptoms, therefore, as they are not aware, the will keep moving.
 *
 *       -Make the possibility to set a lockdown, so a % of the population won't move at all.