# Heap Model

In-process имплементация задачи от OK на Joker 2024

## Suggestions
- Останавливать процесс когда _нагребатор_ не смог добавить ни одного элемента в течении двух спринтов
- Добавить Single-player mode
  - работать за _нагребатора_ напротив _супер-разгребатора_
  - работать за _разгребатора_ напротив _супер-нагребатора_ - в два раза сложнее чем писать _нагребатор_
- Можно разрешить удалять элементы в зависимости от их размера - через 1, 2 и 3 спринта соответственно
  - больше шансов у маленьких элементов быть удаленными
- Можно разрешить перемещать несколько элементов, только чтобы их суммарный размер был не больше 3
