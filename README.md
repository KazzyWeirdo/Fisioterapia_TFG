# Bones pràctiques en enginyeria de software: DDD, TDD, Clean/Hexagonal, CI/CD

## Contexte

Aquest Treball de Fi de Grau (TFG) se centra en crear un sistema backend perquè els professionals de la fisioteràpia puguin registrar de manera digital i centralitzada les rehabilitacions de llarga durada, com la tendinopatia rotuliana crònica. El sistema inclou tant les sessions de radiofreqüència amb la màquina INDIBA com aspectes de psiconeuroimmunologia (PNI) i rehabilitació esportiva.
L’aplicació vol cobrir la necessitat de registrar dades d’aquestes tres àrees de la rehabilitació. Mitjançant una API de les màquines INDIBA i dels dispositius de PNI, la base de dades de l’aplicació es pot omplir amb aquesta informació.
Si no es pot accedir a les API o cal afegir informació sobre la rehabilitació esportiva, els fisioterapeutes poden introduir els detalls de cada sessió, com els paràmetres de tractament, notes o durada, a través d’un formulari al frontend. Aquestes dades s’enviaran al sistema backend per guardar-les.
El sistema també permet consultar l’historial complet d’un pacient, incloent les sessions, la PNI i la rehabilitació esportiva. Això facilita un seguiment detallat del procés i ajuda a prendre decisions clíniques basades en dades.

A més de la funcionalitat en salut, aquest TFG es basa en l’ús d’un conjunt d’arquitectures i bones pràctiques de desenvolupament:
● Arquitectura Hexagonal: Per aïllar la lògica de negoci dels detalls externs, assegurant millor escalabilitat i fàcil manteniment.
● DDD (Domain-Driven Design): Juntament amb l'anterior, per modelar l’aplicació al voltant del domini real de la fisioteràpia.
● TDD (Test-Driven Development): Per guiar el desenvolupament a través de proves abans de fer el codi, garantint la robustesa.
● Principis SOLID: Per garantir un disseny de codi net i flexible.
● CI/CD: Per establir un flux de treball automatitzat d'integració i desplegament.
