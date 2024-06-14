def parse_input_string(input_string):
    lines = input_string.split('\n')
    categorized_preds = {
        'row': [],
        'col': [],
        'cell': [],
        'action': [],
        'item': [],
        'robot': [],
        'objectiveIs': [],
        'costOfNextMove': [],
        'teamColor': [],
        'objective': [],
        'itemAvailable': [],
        'reachingCost': [],
        'allyMine': [],
        'allyRadar': [],
        'enemyRadar': [],
        'enemyMine': [],
        'goodRadarPlace': [],
        'goodMinePlace': [],
        'emptyGem': []  # Aggiunto
    }

    for line in lines:
        if line.strip():
            predicates = line.split('. ')
            for pred in predicates:
                pred = pred.strip()
                if pred.startswith('row('):
                    categorized_preds['row'].append(pred)
                elif pred.startswith('col('):
                    categorized_preds['col'].append(pred)
                elif pred.startswith('cell'):
                    categorized_preds['cell'].append(pred)
                elif pred.startswith('action'):
                    categorized_preds['action'].append(pred)
                elif pred.startswith('item('):
                    categorized_preds['item'].append(pred)
                elif pred.startswith('robot'):
                    categorized_preds['robot'].append(pred)
                elif pred.startswith('objectiveIs'):
                    categorized_preds['objectiveIs'].append(pred)
                elif pred.startswith('costOfNextMove'):
                    categorized_preds['costOfNextMove'].append(pred)
                elif pred.startswith('teamColor'):
                    categorized_preds['teamColor'].append(pred)
                elif pred.startswith('objective('):
                    categorized_preds['objective'].append(pred)
                elif pred.startswith('itemAvailable'):
                    categorized_preds['itemAvailable'].append(pred)
                elif pred.startswith('reachingCost'):
                    categorized_preds['reachingCost'].append(pred)
                elif pred.startswith('allyMine'):
                    categorized_preds['allyMine'].append(pred)
                elif pred.startswith('allyRadar'):
                    categorized_preds['allyRadar'].append(pred)
                elif pred.startswith('enemyRadar'):
                    categorized_preds['enemyRadar'].append(pred)
                elif pred.startswith('enemyMine'):
                    categorized_preds['enemyMine'].append(pred)
                elif pred.startswith('goodRadarPlace'):
                    categorized_preds['goodRadarPlace'].append(pred)
                elif pred.startswith('goodMinePlace'):
                    categorized_preds['goodMinePlace'].append(pred)
                elif pred.startswith('emptyGem'):  # Aggiunto
                    categorized_preds['emptyGem'].append(pred)

    sorted_output = []
    for key in ['row', 'col', 'cell', 'action', 'item', 'robot', 'objectiveIs', 
                'costOfNextMove', 'teamColor', 'objective', 'itemAvailable', 
                'reachingCost', 'allyMine', 'allyRadar', 'enemyRadar', 'enemyMine', 'goodRadarPlace', 'goodMinePlace', 'emptyGem']:  # Aggiunto 'emptyGem'
        if categorized_preds[key]:
            sorted_output.append('. '.join(categorized_preds[key]) + '.')

    output_string = '\n'.join(sorted_output) + '\n'
    output_string = output_string.replace('..\n', '.\n')
    return output_string

if __name__ == "__main__":
    with open("input.txt", "r") as file:
        input_string = file.read()
    
    output_string = parse_input_string(input_string)
    
    with open("input.txt", "w") as file:
        file.write(output_string)
    
    print("Stringa trasformata scritta su 'input.txt'.")
