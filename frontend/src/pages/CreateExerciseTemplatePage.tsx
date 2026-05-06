import { useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faDumbbell, faCirclePlus, faTrash, faXmark, faCheck } from '@fortawesome/free-solid-svg-icons'
import { createExerciseTemplate } from '../services/exerciseTemplateService'
import { useLanguage } from '../contexts/LanguageContext'
import styles from './CreateExerciseTemplatePage.module.css'

interface SetDraft {
  weightKg: string
  reps: string
  restTimeSeconds: string
  rpe: number
}

interface ExerciseDraft {
  id: number
  name: string
  sets: SetDraft[]
}

function newSet(): SetDraft {
  return { weightKg: '', reps: '', restTimeSeconds: '', rpe: 7 }
}

export default function CreateExerciseTemplatePage() {
  const navigate = useNavigate()
  const { t } = useLanguage()

  const nextId = useRef(1)
  function newExercise(): ExerciseDraft {
    return { id: nextId.current++, name: '', sets: [newSet()] }
  }

  const [templateName, setTemplateName] = useState('')
  const [exercises, setExercises] = useState<ExerciseDraft[]>(() => [newExercise()])
  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState<string | null>(null)

  function updateSet(exIdx: number, sIdx: number, field: keyof SetDraft, value: string | number) {
    setExercises(prev => prev.map((ex, i) =>
      i !== exIdx ? ex : {
        ...ex,
        sets: ex.sets.map((s, j) => j !== sIdx ? s : { ...s, [field]: value }),
      }
    ))
  }
  function addSet(exIdx: number) {
    setExercises(prev => prev.map((ex, i) =>
      i !== exIdx ? ex : { ...ex, sets: [...ex.sets, newSet()] }
    ))
  }
  function removeSet(exIdx: number, sIdx: number) {
    setExercises(prev => prev.map((ex, i) =>
      i !== exIdx ? ex : { ...ex, sets: ex.sets.filter((_, j) => j !== sIdx) }
    ))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!templateName) return
    setSubmitting(true)
    setSubmitError(null)
    try {
      await createExerciseTemplate({
        name: templateName,
        exercises: exercises.map(ex => ({
          name: ex.name,
          exercises: ex.sets.map((s, i) => ({
            setNumber: i + 1,
            weightKg: parseFloat(s.weightKg) || 0,
            reps: parseInt(s.reps) || 0,
            restTimeSeconds: parseInt(s.restTimeSeconds) || 0,
            rpe: s.rpe,
          })),
        })),
      })
      navigate(-1)
    } catch {
      setSubmitError(t('template_error'))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <form className={styles.page} onSubmit={handleSubmit}>

      <div>
        <h1 className={styles.heading}>{t('template_register_title')}</h1>
        <p className={styles.subtitle}>{t('template_register_subtitle')}</p>
      </div>

      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faDumbbell} /> {t('template_section_general')}
        </div>
        <div className={styles.field}>
          <label className={styles.label} htmlFor="templateName">{t('template_name_label')}</label>
          <input
            id="templateName"
            type="text"
            className={styles.input}
            placeholder={t('template_name_placeholder')}
            value={templateName}
            onChange={e => setTemplateName(e.target.value)}
            required
          />
        </div>
      </div>

      <div className={styles.exercisesHeader}>
        <span className={styles.exercisesTitle}><FontAwesomeIcon icon={faDumbbell} /> {t('template_section_exercises')}</span>
        <button
          type="button"
          className={styles.addExerciseBtn}
          onClick={() => setExercises(prev => [...prev, newExercise()])}
        >
          <FontAwesomeIcon icon={faCirclePlus} /> {t('template_add_exercise')}
        </button>
      </div>

      {exercises.map((ex, exIdx) => (
        <div key={ex.id} className={styles.exerciseCard}>
          <div className={styles.exerciseCardHeader}>
            <div className={styles.exerciseCardHeaderLeft}>
              <label className={styles.exerciseNameLabel}>{t('template_exercise_name_label')}</label>
              <input
                type="text"
                className={styles.exerciseNameInput}
                placeholder={t('template_exercise_name_placeholder')}
                value={ex.name}
                onChange={e => setExercises(prev => prev.map((x, i) =>
                  i === exIdx ? { ...x, name: e.target.value } : x
                ))}
                required
              />
            </div>
            {exercises.length > 1 && (
              <button
                type="button"
                className={styles.deleteExerciseBtn}
                onClick={() => setExercises(prev => prev.filter((_, i) => i !== exIdx))}
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

      {submitError && <p className={styles.error}>{submitError}</p>}

      <div className={styles.footer}>
        <button
          type="button"
          className={styles.discardBtn}
          onClick={() => navigate(-1)}
        >
          {t('template_discard')}
        </button>
        <button type="submit" className={styles.submitBtn} disabled={submitting}>
          {t('template_submit')}
        </button>
      </div>

    </form>
  )
}
