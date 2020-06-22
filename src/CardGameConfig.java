class CardGameConfig extends GameConfig {
    CardGameRuleSet cardGameRuleSet;

    protected CardGameConfig(GameConfigType configType, CardGameRuleSet cardGameRuleSet) {
        super(configType);
        this.cardGameRuleSet = cardGameRuleSet;
    }

    CardGameRuleSet getRuleSet() {
        return cardGameRuleSet ;
    }
}
