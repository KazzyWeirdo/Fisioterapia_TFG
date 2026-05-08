import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCirclePlus, faTrash, faXmark } from '@fortawesome/free-solid-svg-icons'
import { useLanguage } from '../contexts/LanguageContext'
import styles from './ExerciseList.module.css'

export interface SetDraft {
  weightKg: string | number
  reps: string | number
  restTimeSeconds: string | number
  rpe: number
}

export interface ExerciseDraft {
  id?: number
  name: string
  sets: SetDraft[]
  fromTemplate?: boolean
}

interface ExerciseListProps {
  exercises: ExerciseDraft[]
  onChange: (exercises: ExerciseDraft[]) => void
  readonlyNames?: boolean
}

function newSet(): SetDraft {
  return { weightKg: '', reps: '', restTimeSeconds: '', rpe: 7 }
}

export default function ExerciseList({ exercises, onChange, readonlyNames = false }: ExerciseListProps) {
  const { t } = useLanguage()

  function updateSet(exIdx: number, sIdx: number, field: keyof SetDraft, value: string | number) {
    onChange(exercises.map((ex, i) =>
      i !== exIdx ? ex : {
        ...ex,
        sets: ex.sets.map((s, j) => j !== sIdx ? s : { ...s, [field]: value }),
      }
    ))
  }

  function addSet(exIdx: number) {
    onChange(exercises.map((ex, i) =>
      i !== exIdx ? ex : { ...ex, sets: [...ex.sets, newSet()] }
    ))
  }

  function removeSet(exIdx: number, sIdx: number) {
    onChange(exercises.map((ex, i) =>
      i !== exIdx ? ex : { ...ex, sets: ex.sets.filter((_, j) => j !== sIdx) }
    ))
  }

  function updateExerciseName(exIdx: number, name: string) {
    onChange(exercises.map((ex, i) => i !== exIdx ? ex : { ...ex, name }))
  }

  function removeExercise(exIdx: number) {
    onChange(exercises.filter((_, i) => i !== exIdx))
  }

  function addExercise() {
    onChange([...exercises, { name: '', sets: [newSet()] }])
  }

  return (
    <>
      {exercises.map((ex, exIdx) => (
        <div key={ex.id ?? exIdx} className={styles.exerciseCard}>
          <div className={styles.exerciseCardHeader}>
            <div className={styles.exerciseCardHeaderLeft}>
              <label className={styles.exerciseNameLabel}>{t('template_exercise_name_label')}</label>
              {readonlyNames && ex.fromTemplate
                ? <span className={styles.exerciseNameReadonly}>{ex.name}</span>
                : <input
                    type="text"
                    className={styles.exerciseNameInput}
                    placeholder={t('template_exercise_name_placeholder')}
                    value={ex.name}
                    onChange={e => updateExerciseName(exIdx, e.target.value)}
                    required
                  />
              }
            </div>
            {exercises.length > 1 && (
              <button
                type="button"
                className={styles.deleteExerciseBtn}
                onClick={() => removeExercise(exIdx)}
              >
                <FontAwesomeIcon icon={faTrash} />
              </button>
            )}
          </div>

          <table className={styles.setsTable}>
            <thead>
              <tr>
                <th>{t('training_col_set')}</th>
                <th>{t('training_col_weight')}</th>
                <th>{t('training_col_reps')}</th>
                <th>{t('training_col_rest')}</th>
                <th>{t('template_col_rpe')}</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {ex.sets.map((set, sIdx) => (
                <tr key={sIdx}>
                  <td className={styles.setNum}>{String(sIdx + 1).padStart(2, '0')}</td>
                  <td>
                    <input type="number" min="0" step="0.5" className={styles.setInput}
                      value={set.weightKg}
                      onChange={e => updateSet(exIdx, sIdx, 'weightKg', e.target.value)}
                      required />
                  </td>
                  <td>
                    <input type="number" min="1" className={styles.setInput}
                      value={set.reps}
                      onChange={e => updateSet(exIdx, sIdx, 'reps', e.target.value)}
                      required />
                  </td>
                  <td>
                    <input type="number" min="0" className={styles.setInput}
                      value={set.restTimeSeconds}
                      onChange={e => updateSet(exIdx, sIdx, 'restTimeSeconds', e.target.value)}
                      required />
                  </td>
                  <td className={styles.rpeCell}>
                    <input type="range" min="1" max="10" className={styles.rpeSlider}
                      value={set.rpe}
                      onChange={e => updateSet(exIdx, sIdx, 'rpe', Number(e.target.value))} />
                    <span className={styles.rpeValue}>{set.rpe}</span>
                  </td>
                  <td>
                    {ex.sets.length > 1 && (
                      <button type="button" className={styles.removeSetBtn}
                        onClick={() => removeSet(exIdx, sIdx)}>
                        <FontAwesomeIcon icon={faXmark} />
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <button type="button" className={styles.addSetBtn} onClick={() => addSet(exIdx)}>
            {t('template_add_set')}
          </button>
        </div>
      ))}

      <button type="button" className={styles.addExerciseBtn} onClick={addExercise}>
        <FontAwesomeIcon icon={faCirclePlus} /> {t('training_add_exercise')}
      </button>
    </>
  )
}
